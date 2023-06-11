package antessio.paymentsystem.wallet.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WalletRepository extends JpaRepository<WalletEntity, String>,
                                          JpaSpecificationExecutor<WalletEntity> {

    List<WalletEntity> loadWalletByOwnerId(String ownerId);

}