package antessio.paymentsystem.wallet;

import java.util.List;
import java.util.Optional;

public interface WalletRepository {

    Optional<Wallet> loadWalletById(WalletID id);

    MovementId insertMovement(Movement movement);

    void updateWallet(Wallet wallet);

    WalletID insertWallet(Wallet wallet);

    List<Wallet> loadWalletByOwnerId(WalletOwnerId ownerId);

    Optional<Movement> loadMovementById(MovementId movementId);


}
