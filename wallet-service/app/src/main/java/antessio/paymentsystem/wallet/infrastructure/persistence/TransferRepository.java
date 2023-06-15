package antessio.paymentsystem.wallet.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TransferRepository extends JpaRepository<TransferEntity, String>,
                                            JpaSpecificationExecutor<TransferEntity> {


    @Query(value = "SELECT * FROM transfer WHERE walletId = ?#{#walletId} AND id > ?#{#cursor} ORDER BY id LIMIT ?#{#size}", nativeQuery = true)
    List<TransferEntity> findByWalletIdStartingFrom(String walletId, String cursor, int size);


    List<TransferEntity> findByOperationId(String operationId);


}