package antessio.paymentsystem.wallet.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import antessio.paymentsystem.api.wallet.WalletFundsLockCollectCommand;
import antessio.paymentsystem.api.wallet.WalletFundsLockCommand;
import antessio.paymentsystem.api.wallet.TransferDTO;
import antessio.paymentsystem.api.wallet.WalletDTO;
import antessio.paymentsystem.api.wallet.WalletOwnerFundsLockCollectCommand;
import antessio.paymentsystem.api.wallet.WalletOwnerFundsLockCommand;
import antessio.paymentsystem.api.wallet.WalletService;
import antessio.paymentsystem.common.Message;
import antessio.paymentsystem.common.MessageBroker;
import antessio.paymentsystem.common.SerializationService;
import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.wallet.TransferDirection;
import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwner;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.WalletType;
import antessio.paymentsystem.wallet.domain.Transfer;
import antessio.paymentsystem.wallet.domain.TransferBuilder;
import antessio.paymentsystem.wallet.domain.TransferCreatedEventAdapter;
import antessio.paymentsystem.wallet.domain.TransferDTOAdapter;
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

    private List<TransferDTO> moveMoney(Wallet fromWallet, Wallet toWallet, Amount amount, String sourceOperationId) {

        Transfer sourceTransfer = TransferBuilder.aTransfer()
                                                 .withId(new TransferId(UUID.randomUUID().toString()))
                                                 .withAmount(amount)
                                                 .withDirection(TransferDirection.OUT)
                                                 .withWalletId(fromWallet.getId())
                                                 .withWalletType(fromWallet.getType())
                                                 .withOperationId(sourceOperationId)
                                                 .build();
        Transfer destinationTransfer = TransferBuilder.aTransfer()
                                                      .withId(new TransferId(UUID.randomUUID().toString()))
                                                      .withAmount(amount)
                                                      .withDirection(TransferDirection.IN)
                                                      .withWalletId(toWallet.getId())
                                                      .withWalletType(toWallet.getType())
                                                      .withOperationId(sourceOperationId)
                                                      .build();

        Wallet fromWalletUpdated = WalletBuilder.aWallet()
                                                .withId(fromWallet.getId())
                                                .withOwner(fromWallet.getOwner())
                                                .withType(fromWallet.getType())
                                                .withAmountUnit(fromWallet.getAmountUnit() + getTargetAmount(sourceTransfer))
                                                .build();

        Wallet toWalletUpdated = WalletBuilder.aWallet()
                                              .withId(toWallet.getId())
                                              .withOwner(toWallet.getOwner())
                                              .withType(toWallet.getType())
                                              .withAmountUnit(toWallet.getAmountUnit() + getTargetAmount(destinationTransfer))
                                              .build();

        List<TransferId> transferIds = walletRepository.updateWallet(WalletsUpdateBuilder.aWalletsUpdate()
                                                                                         .withSourceWalletMovement(sourceTransfer)
                                                                                         .withDestinationWalletMovement(destinationTransfer)
                                                                                         .withSourceWallet(fromWalletUpdated)
                                                                                         .withDestinationWallet(toWalletUpdated)
                                                                                         .build());
        Stream<Message> movementEvents = transferIds.stream()
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
                new TransferDTOAdapter(sourceTransfer),
                new TransferDTOAdapter(destinationTransfer)
        );
    }

    @Override
    public TransferId lockFunds(WalletFundsLockCommand command) {
        Wallet wallet = loadWalletById(command.getWalletID());
        return lockFunds(wallet, command.getAmount());
    }


    @Override
    public TransferId lockFunds(WalletOwnerFundsLockCommand command) {
        Wallet wallet = getMainWallet(command.getWalletOwnerId());
        return lockFunds(wallet, command.getAmount());
    }


    private Wallet loadWalletById(WalletID walletID) {
        return walletRepository.loadWalletById(walletID)
                               .orElseThrow(() -> new IllegalArgumentException("wallet id not found"));
    }

    @Override
    public TransferId collectFundLock(WalletFundsLockCollectCommand command) {
        Wallet destinationWallet = walletRepository.loadWalletById(command.getToWallet())
                                                   .orElseThrow(() -> new IllegalArgumentException("to wallet not found " + command.getToWallet().getId()));
        return collectFundLock(command.getFundLockId(), destinationWallet);
    }

    //TODO expires fund lock
    //TODO cancel fund lock
    //TODO charges: start from the receiver

    @Override
    public TransferId collectFundLock(WalletOwnerFundsLockCollectCommand command) {
        Wallet destinationWallet = getMainWallet(command.getToWalletOwnerId());
        return collectFundLock(command.getFundLockId(), destinationWallet);

    }

    @Override
    public Stream<TransferDTO> getTransfers(WalletID walletID) {
        return this.getTransfers(walletID, null);
    }

    @Override
    public Stream<TransferDTO> getTransfers(WalletID walletID, TransferId cursor) {
        Wallet wallet = loadWalletById(walletID);
        return walletRepository.loadTransfersByWalletId(wallet.getId(), cursor)
                               .map(TransferDTOAdapter::new);
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

    private Long getTargetAmount(Transfer sourceTransfer) {
        if (sourceTransfer.getDirection() == TransferDirection.OUT) {
            return sourceTransfer.getAmount().getAmountUnit() * -1;
        }
        return sourceTransfer.getAmount().getAmountUnit();
    }

    private TransferId lockFunds(Wallet wallet, Amount amount) {
        if (wallet.getAmountUnit() < amount.getAmountUnit()) {
            throw new IllegalArgumentException("INSUFFICIENT_AVAILABILITY");
        }
        Wallet fundLockWallet = walletRepository.loadWalletByOwnerId(wallet.getOwnerId())
                                                .stream()
                                                .filter(w -> w.getType() == WalletType.FUND_LOCK)
                                                .findFirst()
                                                .orElseGet(() -> createFundLockWallet(wallet.getOwnerId(), wallet.getOwner()));
        List<TransferDTO> movements = moveMoney(wallet, fundLockWallet, amount, UUID.randomUUID().toString());
        return movements.stream()
                        .filter(m -> m.getWalletID().equals(fundLockWallet.getId()))
                        .findFirst()
                        .map(TransferDTO::getId)
                        .orElseThrow(() -> new RuntimeException("unable to fund fund lock movement"));
    }

    private Wallet getMainWallet(WalletOwnerId walletOwnerId) {
        return walletRepository.loadWalletByOwnerId(walletOwnerId)
                               .stream()
                               .filter(w -> w.getType() == WalletType.MAIN)
                               .findFirst()
                               .orElseThrow(() -> new IllegalArgumentException("MAIN wallet not found"));
    }

    private TransferId collectFundLock(TransferId fundLockId, Wallet destinationWallet) {
        Transfer fundLock = walletRepository.loadTransferById(fundLockId)
                                            .filter(m -> m.getWalletType() == WalletType.FUND_LOCK)
                                            .orElseThrow(() -> new IllegalArgumentException("invalid fund lock id " + fundLockId));

        if (isOperationSumOnWalletIsZero(fundLock.getOperationId(), fundLock.getWalletId())) {
            throw new IllegalArgumentException("fund lock already collected");
        }

        Wallet fundLockWallet = walletRepository.loadWalletById(fundLock.getWalletId())
                                                .orElseThrow(() -> new IllegalArgumentException("from wallet not found " + fundLock.getWalletId()
                                                                                                                                   .getId()));
        List<TransferDTO> transfers = moveMoney(fundLockWallet, destinationWallet, fundLock.getAmount(), fundLock.getOperationId());
        return transfers.get(0).getId();
    }

    private boolean isOperationSumOnWalletIsZero(String operationId, WalletID walletId) {
        Long operationSum = walletRepository.loadTransfersByOperationId(operationId)
                                            .stream()
                                            .filter(t -> t.getWalletId().equals(walletId))
                                            .map(t -> t.getDirection() == TransferDirection.IN
                                                      ? t.getAmount().getAmountUnit()
                                                      : t.getAmount().getAmountUnit() * -1)
                                            .reduce(Long::sum)
                                            .orElse(0L);
        return operationSum == 0L;
    }

}
