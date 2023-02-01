package antessio.paymentsystem.wallet;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import antessio.paymentsystem.api.wallet.LockFundsCommand;
import antessio.paymentsystem.api.wallet.MoveMoneyFromFundLockCommand;
import antessio.paymentsystem.api.wallet.MovementDTO;
import antessio.paymentsystem.api.wallet.WalletDTO;
import antessio.paymentsystem.api.wallet.WalletService;
import antessio.paymentsystem.common.Message;
import antessio.paymentsystem.common.SerializationService;
import antessio.paymentsystem.common.MessageBroker;

public class WalletApplicationService implements WalletService {

    private final WalletRepository walletRepository;


    private final MessageBroker messageBroker;
    private final SerializationService serializationService;

    public WalletApplicationService(WalletRepository walletRepository,
                                    MessageBroker messageBroker,
                                    SerializationService serializationService) {
        this.walletRepository = walletRepository;
        this.messageBroker = messageBroker;
        this.serializationService = serializationService;
    }

    public WalletDTO getWallet(WalletID id) {

        return walletRepository.loadWalletById(id)
                .map(WalletDTOAdapter::new)
                .orElseThrow(()->new IllegalArgumentException("wallet with id "+id.getId()+" not found"));
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
                                                 .withFromWallet(fromWallet.getId())
                                                 .withFromWalletType(fromWallet.getType())
                                                 .withToWallet(toWallet.getId())
                                                 .withToWalletType(toWallet.getType())
                                                 .build();
        Movement destinationMovement = MovementBuilder.aMovement()
                                                      .withId(new MovementId(UUID.randomUUID().toString()))
                                                      .withAmount(command.getAmount())
                                                      .withDirection(MovementDirection.IN)
                                                      .withFromWallet(toWallet.getId())
                                                      .withFromWalletType(toWallet.getType())
                                                      .withToWallet(fromWallet.getId())
                                                      .withToWalletType(fromWallet.getType())
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

        MovementId sourceMovementId = walletRepository.insertMovement(sourceMovement);
        MovementId destinationMovementId = walletRepository.insertMovement(destinationMovement);
        walletRepository.updateWallet(fromWalletUpdated);
        walletRepository.updateWallet(toWalletUpdated);

        messageBroker.sendMessages(
                List.of(
                        Message.of("movement-created", serializationService.serialize(new MovementEventAdapter(sourceMovement))),
                        Message.of("movement-created", serializationService.serialize(new MovementEventAdapter(destinationMovement))),
                        Message.of("wallet-updated", serializationService.serialize(new WalletEventAdapter(fromWalletUpdated))),
                        Message.of("wallet-updated", serializationService.serialize(new WalletEventAdapter(toWalletUpdated)))
                )
        );
        return List.of(
                new MovementDTOAdapter(sourceMovement),
                new MovementDTOAdapter(destinationMovement)
        );
    }

    @Override
    public MovementId lockFunds(LockFundsCommand command) {
        Wallet wallet = loadWalletById(command.getWalletID());
        Wallet fundLockWallet = walletRepository.loadWalletByOwnerId(wallet.getOwnerId())
                                                .stream()
                                                .filter(w -> w.getType() == WalletType.FUND_LOCK)
                                                .findFirst()
                                                .orElseGet(()-> createFundLockWallet(wallet.getOwnerId(), wallet.getOwner()));
        List<MovementDTO> movements = moveMoney(new MoveMoneyCommand(wallet.getId(), fundLockWallet.getId(), command.getAmount()));
        return movements.stream()
                .filter(m->m.getToWallet().equals(fundLockWallet.getId()))
                .findFirst()
                .map(MovementDTO::getId)
                .orElseThrow(()->new RuntimeException("unable to fund fund lock movement"));
    }

    private Wallet loadWalletById(WalletID walletID) {
        return walletRepository.loadWalletById(walletID)
                               .orElseThrow(() -> new IllegalArgumentException("wallet id not found"));
    }

    @Override
    public List<MovementDTO> moveMoneyFromFundLock(MoveMoneyFromFundLockCommand command) {
        Movement fundLock = walletRepository.loadMovementById(command.getFundLockId())
                                            .filter(m -> m.getToWalletType() == WalletType.FUND_LOCK)
                                            .orElseThrow(() -> new IllegalArgumentException("invalid fund lock id " + command.getFundLockId()));

        return moveMoney(new MoveMoneyCommand(fundLock.getFromWallet(), command.getToWallet(), fundLock.getAmount()));
    }

    @Override
    public Stream<MovementDTO> getMovements(WalletID walletID) {
        Wallet wallet = loadWalletById(walletID);
        return walletRepository.loadMovementsByWalletId(wallet.getId())
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
                .orElseThrow(()->new IllegalStateException("wallet not created"));
    }

    private Long getTargetAmount(Movement sourceMovement) {
        if (sourceMovement.getDirection() == MovementDirection.OUT) {
            return sourceMovement.getAmount().getAmountUnit() * -1;
        }
        return sourceMovement.getAmount().getAmountUnit();
    }

}
