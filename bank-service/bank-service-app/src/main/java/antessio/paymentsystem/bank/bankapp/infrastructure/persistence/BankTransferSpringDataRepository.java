package antessio.paymentsystem.bank.bankapp.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import antessio.paymentystem.bank.domain.BankTransfer;

public interface BankTransferSpringDataRepository extends JpaRepository<BankTransferEntity, UUID>,
                                                          JpaSpecificationExecutor<BankTransferEntity> {


    List<BankTransferEntity> findByBankAccountId(UUID bankAccountId, UUID cursor, int limit);


}