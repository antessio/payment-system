package antessio.paymentsystem.wallet.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.domain.Movement;
import antessio.paymentsystem.wallet.domain.Wallet;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.domain.WalletsUpdate;

public interface WalletRepository {

    Optional<Wallet> loadWalletById(WalletID id);


    List<MovementId> updateWallet(WalletsUpdate walletsUpdate);
    WalletID insertWallet(Wallet wallet);

    List<Wallet> loadWalletByOwnerId(WalletOwnerId ownerId);

    Optional<Movement> loadMovementById(MovementId movementId);

    Stream<Movement> loadMovementsByWalletId(WalletID walletID);


}
