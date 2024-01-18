package antessio.paymentsystem.bank.bankapp.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BankAccountSpringDataRepository extends JpaRepository<BankAccountEntity, UUID>,
                                                         JpaSpecificationExecutor<BankAccountEntity> {


    @Query(value = "SELECT * FROM bank-account WHERE owner = ?#{#owner} AND id > ?#{#cursor} ORDER BY id LIMIT ?#{#size}", nativeQuery = true)
    List<BankAccountEntity> findByBankAccountByOwnerStartingFrom(String owner, UUID cursor, int size);


    Optional<BankAccountEntity> findByIban(String iban);


}