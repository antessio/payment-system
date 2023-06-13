package antessio.paymentsystem.wallet.application;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import antessio.paymentsystem.api.wallet.WalletOwnerFundsLockCollectCommand;
import antessio.paymentsystem.api.wallet.WalletOwnerFundsLockCommand;
import antessio.paymentsystem.common.ObjectMapperSerializationService;
import antessio.paymentsystem.common.SerializationService;
import antessio.paymentsystem.common.SimpleMessageBroker;
import antessio.paymentsystem.wallet.Amount;
import antessio.paymentsystem.wallet.MovementDirection;
import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwner;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.WalletType;
import antessio.paymentsystem.wallet.domain.MovementBuilder;
import antessio.paymentsystem.wallet.domain.WalletBuilder;

class WalletApplicationServiceTest {

    private InMemoryWalletRepository walletRepository = new InMemoryWalletRepository();
    private SimpleMessageBroker messageBroker = new SimpleMessageBroker();
    private SerializationService serializationService = new ObjectMapperSerializationService();

    private WalletApplicationService applicationService;


    @BeforeEach
    void setUp() {
        applicationService = new WalletApplicationService(walletRepository, messageBroker, serializationService);
    }

    @AfterEach
    void tearDown() {
        walletRepository.cleanup();
    }

    @Test
    void shouldLockFunds() {
        // given
        Amount amount = new Amount(100L, "EUR");
        WalletID walletId = new WalletID(UUID.randomUUID().toString());
        long initialWalletAmount = 400_00L;
        WalletOwnerId walletOwnerId = new WalletOwnerId(UUID.randomUUID().toString());
        createMainWallet(walletId, initialWalletAmount, walletOwnerId);

        // when
        applicationService.lockFunds(new WalletOwnerFundsLockCommand(amount, walletOwnerId));

        //then
        assertWallets(walletOwnerId, new Tuple[]{
                tuple(initialWalletAmount - amount.getAmountUnit(), WalletType.MAIN),
                tuple(amount.getAmountUnit(), WalletType.FUND_LOCK)
        });
    }

    @Test
    void shouldRaiseErrorOnFundLockInsufficientAvailability() {
        Amount amount = new Amount(30_00L, "EUR");
        WalletID walletId = new WalletID(UUID.randomUUID().toString());
        long initialWalletAmount = 1_00L;
        WalletOwnerId walletOwnerId = new WalletOwnerId(UUID.randomUUID().toString());
        createMainWallet(walletId, initialWalletAmount, walletOwnerId);

        // when
        //then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                  .isThrownBy(() -> applicationService.lockFunds(new WalletOwnerFundsLockCommand(amount, walletOwnerId)))
                  .withMessage("INSUFFICIENT_AVAILABILITY");
        Assertions.assertThat(walletRepository.loadWalletByOwnerId(walletOwnerId))
                  .extracting(w -> tuple(w.getAmountUnit(), w.getType()))
                  .containsExactly(
                          tuple(initialWalletAmount, WalletType.MAIN));
    }

    @Test
    void shouldRaiseErrorOnFundLockWalletNotFound() {
        Amount amount = new Amount(30_00L, "EUR");
        long initialWalletAmount = 1_00L;
        WalletOwnerId walletOwnerId = new WalletOwnerId(UUID.randomUUID().toString());
        // when
        //then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                  .isThrownBy(() -> applicationService.lockFunds(new WalletOwnerFundsLockCommand(amount, walletOwnerId)))
                  .withMessage("MAIN wallet not found");

    }


    @Test
    void shouldCollectFundLock() {
        // given
        WalletID mainWalletId = new WalletID(UUID.randomUUID().toString());
        WalletID destinationWallet = new WalletID(UUID.randomUUID().toString());
        WalletID fundLockWalletId = new WalletID(UUID.randomUUID().toString());
        WalletOwnerId walletOwnerId = new WalletOwnerId(UUID.randomUUID().toString());
        WalletOwnerId destinationWalletOwnerId = new WalletOwnerId(UUID.randomUUID().toString());
        long fundLockAmount = 20_00L;
        long sourceWalletAmount = 400_00L - fundLockAmount;
        long destinationWalletAmount = 10L;
        createMainWallet(mainWalletId, sourceWalletAmount, walletOwnerId);
        createMainWallet(destinationWallet, destinationWalletAmount, destinationWalletOwnerId);
        createFundLockWallet(fundLockWalletId, fundLockAmount, walletOwnerId);
        MovementId fundLockId = moveMoneyToFundLock(fundLockWalletId, fundLockAmount);
        moveMoneyFromMainWallet(mainWalletId, fundLockAmount);

        // when
        applicationService.collectFundLock(WalletOwnerFundsLockCollectCommand.of(fundLockId, destinationWalletOwnerId));

        // then
        assertWallets(
                walletOwnerId,
                tuple(sourceWalletAmount, WalletType.MAIN),
                tuple(0L, WalletType.FUND_LOCK));

        assertWallets(
                destinationWalletOwnerId,
                tuple(destinationWalletAmount + fundLockAmount, WalletType.MAIN));
    }

    private void assertWallets(WalletOwnerId walletOwnerId, Tuple... wallets) {
        Assertions.assertThat(walletRepository.loadWalletByOwnerId(walletOwnerId))
                  .extracting(w -> tuple(w.getAmountUnit(), w.getType()))
                  .containsExactlyInAnyOrder(wallets);
    }

    private void moveMoneyFromMainWallet(WalletID mainWalletId, long fundLockAmount) {
        walletRepository.insertMovement(MovementBuilder.aMovement()
                                                       .withWalletType(WalletType.MAIN)
                                                       .withWalletId(mainWalletId)
                                                       .withAmount(new Amount(fundLockAmount, "EUR"))
                                                       .withDirection(MovementDirection.OUT)
                                                       .withId(new MovementId(UUID.randomUUID().toString()))
                                                       .build());
    }

    private MovementId moveMoneyToFundLock(WalletID fundLockWalletId, long fundLockAmount) {
        MovementId fundLockId;
        fundLockId = walletRepository.insertMovement(MovementBuilder.aMovement()
                                                                    .withWalletId(fundLockWalletId)
                                                                    .withWalletType(WalletType.FUND_LOCK)
                                                                    .withAmount(new Amount(fundLockAmount, "EUR"))
                                                                    .withDirection(MovementDirection.IN)
                                                                    .withId(new MovementId(UUID.randomUUID().toString()))
                                                                    .build());
        return fundLockId;
    }

    private void createFundLockWallet(WalletID fundLockWalletId, long fundLockAmount, WalletOwnerId walletOwnerId) {
        createConsumerWallet(fundLockWalletId, fundLockAmount, walletOwnerId, WalletType.FUND_LOCK);
    }

    private void createMainWallet(WalletID walletId, long initialWalletAmount, WalletOwnerId walletOwnerId) {
        createConsumerWallet(walletId, initialWalletAmount, walletOwnerId, WalletType.MAIN);
    }

    private WalletID createConsumerWallet(WalletID walletId, long amountUnit, WalletOwnerId ownerId, WalletType main) {
        return walletRepository.insertWallet(WalletBuilder.aWallet()
                                                          .withAmountUnit(amountUnit)
                                                          .withId(walletId)
                                                          .withOwner(WalletOwner.CONSUMER)
                                                          .withOwnerId(ownerId)
                                                          .withType(main)
                                                          .build());
    }

}