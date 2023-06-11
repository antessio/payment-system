package antessio.paymentsystem.wallet.application;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import antessio.paymentsystem.api.wallet.LockFundsCommand;
import antessio.paymentsystem.api.wallet.CollectFundLockCommand;
import antessio.paymentsystem.api.wallet.MovementDTO;
import antessio.paymentsystem.api.wallet.WalletDTO;
import antessio.paymentsystem.api.wallet.WalletService;
import antessio.paymentsystem.common.Message;
import antessio.paymentsystem.common.SerializationService;
import antessio.paymentsystem.common.MessageBroker;
import antessio.paymentsystem.wallet.Amount;
import antessio.paymentsystem.wallet.MovementDirection;
import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwner;
import antessio.paymentsystem.wallet.WalletType;
import antessio.paymentsystem.wallet.domain.Movement;
import antessio.paymentsystem.wallet.domain.MovementBuilder;
import antessio.paymentsystem.wallet.domain.MovementCreatedEventAdapter;
import antessio.paymentsystem.wallet.domain.MovementDTOAdapter;
import antessio.paymentsystem.wallet.domain.Wallet;
import antessio.paymentsystem.wallet.domain.WalletBuilder;
import antessio.paymentsystem.wallet.domain.WalletDTOAdapter;
import antessio.paymentsystem.wallet.domain.WalletEventAdapter;
import antessio.paymentsystem.wallet.WalletOwnerId;
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

    private List<MovementDTO> moveMoney(final MoveMoneyCommand command) {
        Wallet fromWallet = walletRepository.loadWalletById(command.getFromWallet())
                                            .orElseThrow(() -> new IllegalArgumentException("from wallet not found " + command.getFromWallet().getId()));

        Wallet toWallet = walletRepository.loadWalletById(command.getToWallet())
                                          .orElseThrow(() -> new IllegalArgumentException("to wallet not found " + command.getToWallet().getId()));

        Movement sourceMovement = MovementBuilder.aMovement()
                                                 .withId(new MovementId(UUID.randomUUID().toString()))
                                                 .withAmount(command.getAmount())
                                                 .withDirection(MovementDirection.OUT)
                                                 .withWalletId(fromWallet.getId())
                                                 .withWalletType(fromWallet.getType())
                                                 .build();
        Movement destinationMovement = MovementBuilder.aMovement()
                                                      .withId(new MovementId(UUID.randomUUID().toString()))
                                                      .withAmount(command.getAmount())
                                                      .withDirection(MovementDirection.IN)
                                                      .withWalletId(toWallet.getId())
                                                      .withWalletType(toWallet.getType())
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
                                                                                         .withSourceWallet(fromWallet)
                                                                                         .withDestinationWallet(toWallet)
                                                                                         .build());
        Stream<Message> movementEvents = movementIds.stream()
                                                    .map(walletRepository::loadMovementById)
                                                    .filter(Optional::isPresent)
                                                    .map(Optional::get)
                                                    .map(MovementCreatedEventAdapter::new)
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
/*        return List.of(
                new MovementDTOAdapter(sourceMovement),
                new MovementDTOAdapter(destinationMovement)
        );*/
        return Collections.emptyList();
    }

    @Override
    public MovementId lockFunds(LockFundsCommand command) {
        Wallet wallet = loadWalletById(command.getWalletID());
        Wallet fundLockWallet = walletRepository.loadWalletByOwnerId(wallet.getOwnerId())
                                                .stream()
                                                .filter(w -> w.getType() == WalletType.FUND_LOCK)
                                                .findFirst()
                                                .orElseGet(() -> createFundLockWallet(wallet.getOwnerId(), wallet.getOwner()));
        List<MovementDTO> movements = moveMoney(new MoveMoneyCommand(wallet.getId(), fundLockWallet.getId(), command.getAmount()));
        return movements.stream()
                        .filter(m -> m.getToWallet().equals(fundLockWallet.getId()))
                        .findFirst()
                        .map(MovementDTO::getId)
                        .orElseThrow(() -> new RuntimeException("unable to fund fund lock movement"));
    }

    private Wallet loadWalletById(WalletID walletID) {
        return walletRepository.loadWalletById(walletID)
                               .orElseThrow(() -> new IllegalArgumentException("wallet id not found"));
    }

    @Override
    public MovementId collectFundLock(CollectFundLockCommand command) {
        Movement fundLock = walletRepository.loadMovementById(command.getFundLockId())
                                            .filter(m -> m.getWalletType() == WalletType.FUND_LOCK)
                                            .orElseThrow(() -> new IllegalArgumentException("invalid fund lock id " + command.getFundLockId()));

        List<MovementDTO> movements = moveMoney(new MoveMoneyCommand(fundLock.getWalletId(), command.getToWallet(), fundLock.getAmount()));
        return movements.get(0).getId();
    }

    @Override
    public Stream<MovementDTO> getMovements(WalletID walletID) {
        Wallet wallet = loadWalletById(walletID);
        return Stream.empty();
//        return walletRepository.loadMovementsByWalletId(wallet.getId())
//                               .map(MovementDTOAdapter::new);
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

    static class MoveMoneyCommand {

        private WalletID fromWallet;
        private WalletID toWallet;
        private Amount amount;

        public MoveMoneyCommand(WalletID fromWallet, WalletID toWallet, Amount amount) {
            this.fromWallet = fromWallet;
            this.toWallet = toWallet;
            this.amount = amount;
        }

        public WalletID getFromWallet() {
            return fromWallet;
        }

        public WalletID getToWallet() {
            return toWallet;
        }

        public Amount getAmount() {
            return amount;
        }

    }

}
