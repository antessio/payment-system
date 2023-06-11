package antessio.paymentsystem.wallet.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import antessio.paymentsystem.wallet.domain.Wallet;

public interface MovementRepository extends JpaRepository<MovementEntity, String>,
                                            JpaSpecificationExecutor<MovementEntity> {


    @Query(value = "SELECT * FROM movement WHERE walletId = ?#{#walletId} AND id > ?#{#cursor} ORDER BY id LIMIT ?#{#size}", nativeQuery = true)
    List<MovementEntity> findByWalletIdStartingFrom(String walletId, String cursor, int size);



}