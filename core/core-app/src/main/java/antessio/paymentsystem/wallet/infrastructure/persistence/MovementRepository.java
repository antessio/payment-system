package antessio.paymentsystem.wallet.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface MovementRepository extends JpaRepository<MovementEntity, String>,
                                            JpaSpecificationExecutor<MovementEntity> {


    @Query(value = "SELECT * FROM movement WHERE walletId = ?#{#walletId} AND id > ?#{#cursor} ORDER BY id LIMIT ?#{#size}", nativeQuery = true)
    List<MovementEntity> findByWalletIdStartingFrom(String walletId, String cursor, int size);


    List<MovementEntity> findByOperationId(String operationId);


}