package antessio.paymentsystem.wallet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import antessio.paymentsystem.common.MessageBroker;
import antessio.paymentsystem.common.SerializationService;
import antessio.paymentsystem.wallet.application.WalletApplicationService;
import antessio.paymentsystem.wallet.infrastructure.persistence.WalletRepositoryAdapter;

@Configuration
public class WalletConfiguration {

    @Bean
    public WalletApplicationService walletApplicationService(
            WalletRepositoryAdapter walletRepositoryAdapter,
            MessageBroker messageBroker,
            SerializationService serializationService) {
        return new WalletApplicationService(
                walletRepositoryAdapter,
                messageBroker,
                serializationService);
    }

}
