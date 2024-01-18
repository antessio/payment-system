package antessio.paymentsystem.bank.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface BankTransferSpringDataRepository extends JpaRepository<BankTransferEntity, String>,
                                                          JpaSpecificationExecutor<BankTransferEntity> {

    @Query(value = "SELECT * FROM bank-transfer WHERE bankAccountId = ?#{#bankAccountId} AND id > ?#{#cursor} ORDER BY id LIMIT ?#{#size}", nativeQuery = true)
    List<BankTransferEntity> findByBankAccountId(UUID bankAccountId, String cursor, int size);


}