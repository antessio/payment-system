package antessio.paymentsystem.wallet.application;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import antessio.paymentsystem.api.wallet.WalletOwnerFundsLockCommand;
import antessio.paymentsystem.common.ObjectMapperSerializationService;
import antessio.paymentsystem.common.SerializationService;
import antessio.paymentsystem.common.SimpleMessageBroker;
import antessio.paymentsystem.wallet.Amount;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwner;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.WalletType;
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
        createConsumerWallet(walletId, initialWalletAmount, walletOwnerId);

        // when
        applicationService.lockFunds(new WalletOwnerFundsLockCommand(amount, walletOwnerId));

        //then
        Assertions.assertThat(walletRepository.loadWalletByOwnerId(walletOwnerId))
                  .extracting(w -> tuple(w.getAmountUnit(), w.getType()))
                  .containsExactly(
                          tuple(initialWalletAmount - amount.getAmountUnit(), WalletType.MAIN),
                          tuple(amount.getAmountUnit(), WalletType.FUND_LOCK));
    }

    @Test
    void shouldRaiseErrorOnFundLockInsufficientAvailability() {
        Amount amount = new Amount(30_00L, "EUR");
        WalletID walletId = new WalletID(UUID.randomUUID().toString());
        long initialWalletAmount = 1_00L;
        WalletOwnerId walletOwnerId = new WalletOwnerId(UUID.randomUUID().toString());
        createConsumerWallet(walletId, initialWalletAmount, walletOwnerId);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                          .isThrownBy(() ->applicationService.lockFunds(new WalletOwnerFundsLockCommand(amount, walletOwnerId)))
                .withMessage("INSUFFICIENT_AVAILABILITY");

        //then
        Assertions.assertThat(walletRepository.loadWalletByOwnerId(walletOwnerId))
                  .extracting(w -> tuple(w.getAmountUnit(), w.getType()))
                  .containsExactly(
                          tuple(initialWalletAmount, WalletType.MAIN));
    }

    private WalletID createConsumerWallet(WalletID walletId, long amountUnit, WalletOwnerId ownerId) {
        return walletRepository.insertWallet(WalletBuilder.aWallet()
                                                          .withAmountUnit(amountUnit)
                                                          .withId(walletId)
                                                          .withOwner(WalletOwner.CONSUMER)
                                                          .withOwnerId(ownerId)
                                                          .withType(WalletType.MAIN)
                                                          .build());
    }

}