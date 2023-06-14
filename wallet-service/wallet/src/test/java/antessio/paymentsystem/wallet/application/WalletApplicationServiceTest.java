package antessio.paymentsystem.wallet.application;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import java.util.List;
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
import antessio.paymentsystem.wallet.TransferDirection;
import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwner;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.WalletType;
import antessio.paymentsystem.wallet.domain.Transfer;
import antessio.paymentsystem.wallet.domain.TransferBuilder;
import antessio.paymentsystem.wallet.domain.WalletBuilder;

class WalletApplicationServiceTest {

    private final InMemoryWalletRepository walletRepository = new InMemoryWalletRepository();
    private final SimpleMessageBroker messageBroker = new SimpleMessageBroker();
    private final SerializationService serializationService = new ObjectMapperSerializationService();

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
        assertWallets(
                walletOwnerId,
                tuple(initialWalletAmount - amount.getAmountUnit(), WalletType.MAIN),
                tuple(amount.getAmountUnit(), WalletType.FUND_LOCK));

        assertTransfers(
                walletOwnerId,
                tuple(amount.getAmountUnit(), TransferDirection.IN, WalletType.FUND_LOCK),
                tuple(amount.getAmountUnit(), TransferDirection.OUT, WalletType.MAIN));


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
        assertWallets(walletOwnerId, tuple(initialWalletAmount, WalletType.MAIN));
        assertTransfers(walletOwnerId);
    }

    @Test
    void shouldRaiseErrorOnFundLockWalletNotFound() {
        Amount amount = new Amount(30_00L, "EUR");
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
        TransferId fundLockId = moveMoneyToFundLock(fundLockWalletId, fundLockAmount);
        moveMoneyFromMainWallet(mainWalletId, fundLockAmount, UUID.randomUUID().toString());

        // when
        applicationService.collectFundLock(WalletOwnerFundsLockCollectCommand.of(fundLockId, destinationWalletOwnerId));

        // then
        assertWallets(
                walletOwnerId,
                tuple(sourceWalletAmount, WalletType.MAIN),
                tuple(0L, WalletType.FUND_LOCK));

        assertTransfers(
                walletOwnerId,
                tuple(fundLockAmount, TransferDirection.OUT, WalletType.MAIN),
                tuple(fundLockAmount, TransferDirection.IN, WalletType.FUND_LOCK),
                tuple(fundLockAmount, TransferDirection.OUT, WalletType.FUND_LOCK));

        assertWallets(
                destinationWalletOwnerId,
                tuple(destinationWalletAmount + fundLockAmount, WalletType.MAIN));

        assertTransfers(
                destinationWalletOwnerId,
                tuple(fundLockAmount, TransferDirection.IN, WalletType.MAIN));
    }

    @Test
    void shouldRaiseErrorFundLockAlreadyCollected() {
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
        TransferId fundLockId = moveMoneyToFundLock(fundLockWalletId, fundLockAmount);
        moveMoneyFromMainWallet(mainWalletId, fundLockAmount, UUID.randomUUID().toString());

        // when
        applicationService.collectFundLock(WalletOwnerFundsLockCollectCommand.of(fundLockId, destinationWalletOwnerId));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                  .isThrownBy(() -> applicationService.collectFundLock(WalletOwnerFundsLockCollectCommand.of(fundLockId, destinationWalletOwnerId)));

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

    private void moveMoneyFromMainWallet(WalletID mainWalletId, long fundLockAmount, String operationId) {
        walletRepository.insertTransfer(TransferBuilder.aTransfer()
                                                       .withWalletType(WalletType.MAIN)
                                                       .withWalletId(mainWalletId)
                                                       .withAmount(new Amount(fundLockAmount, "EUR"))
                                                       .withDirection(TransferDirection.OUT)
                                                       .withId(new TransferId(UUID.randomUUID().toString()))
                                                       .withOperationId(operationId)
                                                       .build());
    }

    private TransferId moveMoneyToFundLock(WalletID fundLockWalletId, long fundLockAmount) {
        TransferId fundLockId;
        fundLockId = walletRepository.insertTransfer(TransferBuilder.aTransfer()
                                                                    .withWalletId(fundLockWalletId)
                                                                    .withWalletType(WalletType.FUND_LOCK)
                                                                    .withAmount(new Amount(fundLockAmount, "EUR"))
                                                                    .withDirection(TransferDirection.IN)
                                                                    .withId(new TransferId(UUID.randomUUID().toString()))
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

    private void assertTransfers(WalletOwnerId walletOwnerId, Tuple... expectedTransfers) {
        List<Transfer> transfersByWalletOwnerId = walletRepository.getTransfersByWalletOwnerId(walletOwnerId);
        if (expectedTransfers.length > 0) {
            Assertions.assertThat(transfersByWalletOwnerId)
                      .extracting(t -> tuple(t.getAmount().getAmountUnit(), t.getDirection(), t.getWalletType()))
                      .containsExactlyInAnyOrder(expectedTransfers);
        } else {
            Assertions.assertThat(transfersByWalletOwnerId).hasSize(0);

        }
    }

}