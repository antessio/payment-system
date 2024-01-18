package antessio.paymentsystem.wallet.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import antessio.paymentsystem.api.wallet.WalletFundsLockCollectCommand;
import antessio.paymentsystem.api.wallet.WalletFundsLockCommand;
import antessio.paymentsystem.api.wallet.MovementDTO;
import antessio.paymentsystem.api.wallet.WalletDTO;
import antessio.paymentsystem.api.wallet.WalletOwnerFundsLockCollectCommand;
import antessio.paymentsystem.api.wallet.WalletOwnerFundsLockCommand;
import antessio.paymentsystem.api.wallet.WalletService;
import antessio.paymentsystem.common.Message;
import antessio.paymentsystem.common.MessageBroker;
import antessio.paymentsystem.common.SerializationService;
import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.wallet.MovementDirection;
import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwner;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.WalletType;
import antessio.paymentsystem.wallet.domain.Movement;
import antessio.paymentsystem.wallet.domain.TransferBuilder;
import antessio.paymentsystem.wallet.domain.TransferCreatedEventAdapter;
import antessio.paymentsystem.wallet.domain.MovementDTOAdapter;
import antessio.paymentsystem.wallet.domain.Wallet;
import antessio.paymentsystem.wallet.domain.WalletBuilder;
import antessio.paymentsystem.wallet.domain.WalletDTOAdapter;
import antessio.paymentsystem.wallet.domain.WalletEventAdapter;
import antessio.paymentsystem.wallet.domain.WalletsUpdateBuilder;

public class WalletApplicationService implements WalletService {

    private final WalletRepository walletRepository;
    private final MessageBroker messageBroker;
    private final SerializationService serializationService;

    public WalletApplicationService(
            WalletRepository walletRepository,
            MessageBroker messageBroker,
            SerializationService serializationService) {
        this.walletRepository = walletRepository;
        this.messageBroker = messageBroker;
        this.serializationService = serializationService;
    }

    public WalletDTO getWallet(WalletID id) {

        return walletRepository.loadWalletById(id)
                               .map(WalletDTOAdapter::new)
                               .orElseThrow(() -> new IllegalArgumentException("wallet with id " + id.getId() + " not found"));
    }

    @Override
    public List<WalletDTO> getWalletsByWalletOwner(WalletOwnerId walletOwnerId) {
        return walletRepository.loadWalletByOwnerId(walletOwnerId)
                .stream()
                .map(WalletDTOAdapter::new)
                .collect(Collectors.toList());
    }

    private List<MovementDTO> moveMoney(Wallet fromWallet, Wallet toWallet, Amount amount, String sourceOperationId) {

        Movement sourceMovement = TransferBuilder.aTransfer()
                                                 .withId(new MovementId(UUID.randomUUID().toString()))
                                                 .withAmount(amount)
                                                 .withDirection(MovementDirection.OUT)
                                                 .withWalletId(fromWallet.getId())
                                                 .withWalletType(fromWallet.getType())
                                                 .withOperationId(sourceOperationId)
                                                 .build();
        Movement destinationMovement = TransferBuilder.aTransfer()
                                                      .withId(new MovementId(UUID.randomUUID().toString()))
                                                      .withAmount(amount)
                                                      .withDirection(MovementDirection.IN)
                                                      .withWalletId(toWallet.getId())
                                                      .withWalletType(toWallet.getType())
                                                      .withOperationId(sourceOperationId)
                                                      .build();

        Wallet fromWalletUpdated = WalletBuilder.aWallet()
                                                .withId(fromWallet.getId())
                                                .withOwner(fromWallet.getOwner())
                                                .withType(fromWallet.getType())
                                                .withAmountUnit(fromWallet.getAmountUnit() + getTargetAmount(sourceMovement))
                                                .build();

        Wallet toWalletUpdated = WalletBuilder.aWallet()
                                              .withId(toWallet.getId())
                                              .withOwner(toWallet.getOwner())
                                              .withType(toWallet.getType())
                                              .withAmountUnit(toWallet.getAmountUnit() + getTargetAmount(destinationMovement))
                                              .build();

        List<MovementId> movementIds = walletRepository.updateWallet(WalletsUpdateBuilder.aWalletsUpdate()
                                                                                         .withSourceWalletMovement(sourceMovement)
                                                                                         .withDestinationWalletMovement(destinationMovement)
                                                                                         .withSourceWallet(fromWalletUpdated)
                                                                                         .withDestinationWallet(toWalletUpdated)
                                                                                         .build());
        Stream<Message> movementEvents = movementIds.stream()
                                                    .map(walletRepository::loadTransferById)
                                                    .filter(Optional::isPresent)
                                                    .map(Optional::get)
                                                    .map(TransferCreatedEventAdapter::new)
                                                    .map(serializationService::serialize)
                                                    .map(m -> Message.of("movement-created", m));

        Stream<Message> walletEvents = Stream.of(fromWalletUpdated, toWalletUpdated)
                                             .map(WalletEventAdapter::new)
                                             .map(serializationService::serialize)
                                             .map(m -> Message.of("wallet-updated", m));

        messageBroker.sendMessages(
                Stream.concat(
                              movementEvents,
                              walletEvents)
                      .toList()
        );
        return List.of(
                new MovementDTOAdapter(sourceMovement),
                new MovementDTOAdapter(destinationMovement)
        );
    }

    @Override
    public MovementId lockFunds(WalletFundsLockCommand command) {
        Wallet wallet = loadWalletById(command.getWalletID());
        return lockFunds(wallet, command.getAmount());
    }


    @Override
    public MovementId lockFunds(WalletOwnerFundsLockCommand command) {
        Wallet wallet = getMainWallet(command.getWalletOwnerId());
        return lockFunds(wallet, command.getAmount());
    }


    private Wallet loadWalletById(WalletID walletID) {
        return walletRepository.loadWalletById(walletID)
                               .orElseThrow(() -> new IllegalArgumentException("wallet id not found"));
    }

    @Override
    public MovementId collectFundLock(WalletFundsLockCollectCommand command) {
        Wallet destinationWallet = walletRepository.loadWalletById(command.getToWallet())
                                                   .orElseThrow(() -> new IllegalArgumentException("to wallet not found " + command.getToWallet().getId()));
        return collectFundLock(command.getFundLockId(), destinationWallet);
    }

    //TODO expires fund lock
    //TODO cancel fund lock
    //TODO charges: start from the receiver

    @Override
    public MovementId collectFundLock(WalletOwnerFundsLockCollectCommand command) {
        Wallet destinationWallet = getMainWallet(command.getToWalletOwnerId());
        return collectFundLock(command.getFundLockId(), destinationWallet);

    }

    @Override
    public Stream<MovementDTO> getMovements(WalletID walletID) {
        return this.getMovements(walletID, null);
    }

    @Override
    public Stream<MovementDTO> getMovements(WalletID walletID, MovementId cursor) {
        Wallet wallet = loadWalletById(walletID);
        return walletRepository.loadTransfersByWalletId(wallet.getId(), cursor)
                               .map(MovementDTOAdapter::new);
    }

    private Wallet createFundLockWallet(WalletOwnerId ownerId, WalletOwner owner) {
        WalletID walletID = walletRepository.insertWallet(WalletBuilder
                                                                  .aWallet()
                                                                  .withAmountUnit(0L)
                                                                  .withOwnerId(ownerId)
                                                                  .withId(new WalletID(UUID.randomUUID().toString()))
                                                                  .withOwner(owner)
                                                                  .withType(WalletType.FUND_LOCK)
                                                                  .build());
        return walletRepository.loadWalletById(walletID)
                               .orElseThrow(() -> new IllegalStateException("wallet not created"));
    }

    private Long getTargetAmount(Movement sourceMovement) {
        if (sourceMovement.getDirection() == MovementDirection.OUT) {
            return sourceMovement.getAmount().getAmountUnit() * -1;
        }
        return sourceMovement.getAmount().getAmountUnit();
    }

    private MovementId lockFunds(Wallet wallet, Amount amount) {
        if (wallet.getAmountUnit() < amount.getAmountUnit()) {
            throw new IllegalArgumentException("INSUFFICIENT_AVAILABILITY");
        }
        Wallet fundLockWallet = walletRepository.loadWalletByOwnerId(wallet.getOwnerId())
                                                .stream()
                                                .filter(w -> w.getType() == WalletType.FUND_LOCK)
                                                .findFirst()
                                                .orElseGet(() -> createFundLockWallet(wallet.getOwnerId(), wallet.getOwner()));
        List<MovementDTO> movements = moveMoney(wallet, fundLockWallet, amount, UUID.randomUUID().toString());
        return movements.stream()
                        .filter(m -> m.getWalletID().equals(fundLockWallet.getId()))
                        .findFirst()
                        .map(MovementDTO::getId)
                        .orElseThrow(() -> new RuntimeException("unable to fund fund lock movement"));
    }

    private Wallet getMainWallet(WalletOwnerId walletOwnerId) {
        return walletRepository.loadWalletByOwnerId(walletOwnerId)
                               .stream()
                               .filter(w -> w.getType() == WalletType.MAIN)
                               .findFirst()
                               .orElseThrow(() -> new IllegalArgumentException("MAIN wallet not found"));
    }

    private MovementId collectFundLock(MovementId fundLockId, Wallet destinationWallet) {
        Movement fundLock = walletRepository.loadTransferById(fundLockId)
                                            .filter(m -> m.getWalletType() == WalletType.FUND_LOCK)
                                            .orElseThrow(() -> new IllegalArgumentException("invalid fund lock id " + fundLockId));

        if (isOperationSumOnWalletIsZero(fundLock.getOperationId(), fundLock.getWalletId())) {
            throw new IllegalArgumentException("fund lock already collected");
        }

        Wallet fundLockWallet = walletRepository.loadWalletById(fundLock.getWalletId())
                                                .orElseThrow(() -> new IllegalArgumentException("from wallet not found " + fundLock.getWalletId()
                                                                                                                                   .getId()));
        List<MovementDTO> transfers = moveMoney(fundLockWallet, destinationWallet, fundLock.getAmount(), fundLock.getOperationId());
        return transfers.get(0).getId();
    }

    private boolean isOperationSumOnWalletIsZero(String operationId, WalletID walletId) {
        Long operationSum = walletRepository.loadTransfersByOperationId(operationId)
                                            .stream()
                                            .filter(t -> t.getWalletId().equals(walletId))
                                            .map(t -> t.getDirection() == MovementDirection.IN
                                                      ? t.getAmount().getAmountUnit()
                                                      : t.getAmount().getAmountUnit() * -1)
                                            .reduce(Long::sum)
                                            .orElse(0L);
        return operationSum == 0L;
    }

}