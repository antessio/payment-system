package antessio.paymentsystem.bank;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import antessio.paymentsystem.bank.infrastructure.persistence.BankAccountRepositoryAdapter;
import antessio.paymentsystem.bank.infrastructure.persistence.BankTransferRepositoryAdapter;
import antessio.paymentsystem.common.MessageBroker;
import antessio.paymentsystem.common.SerializationService;
import antessio.paymentystem.bank.application.BankAccountApplicationService;
import antessio.paymentystem.bank.application.BankTransferApplicationService;
import antessio.paymentystem.bank.domain.BankTransferDomainService;

@Configuration
public class BankAppConfiguration {

    @Bean
    public BankAccountApplicationService bankAccountApplicationService(BankAccountRepositoryAdapter bankAccountRepositoryAdapter) {
        return new BankAccountApplicationService(bankAccountRepositoryAdapter);
    }

    @Bean
    public BankTransferApplicationService bankTransferApplicationService(
            BankTransferRepositoryAdapter bankTransferRepositoryAdapter,
            BankTransferDomainService bankTransferDomainService) {
        return new BankTransferApplicationService(
                bankTransferDomainService,
                bankTransferRepositoryAdapter);
    }

    @Bean
    public BankTransferDomainService bankTransferDomainService(
            BankTransferRepositoryAdapter bankTransferRepositoryAdapter,
            MessageBroker messageBroker,
            SerializationService serializationService) {
        return new BankTransferDomainService(bankTransferRepositoryAdapter,
                                             messageBroker, serializationService);
    }

}
