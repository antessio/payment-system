package antessio.paymentsystem.wallet.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.domain.Transfer;
import antessio.paymentsystem.wallet.domain.Wallet;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.domain.WalletsUpdate;

public interface WalletRepository {

    Optional<Wallet> loadWalletById(WalletID id);


    List<TransferId> updateWallet(WalletsUpdate walletsUpdate);
    WalletID insertWallet(Wallet wallet);

    List<Wallet> loadWalletByOwnerId(WalletOwnerId ownerId);

    Optional<Transfer> loadTransferById(TransferId transferId);

    Stream<Transfer> loadTransfersByWalletId(WalletID walletID);

    List<Transfer> loadTransfersByOperationId(String operationId);




}
