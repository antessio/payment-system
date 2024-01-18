package antessio.paymentsystem.api.wallet;

import java.util.List;
import java.util.stream.Stream;

import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwnerId;


public interface WalletService {

    WalletDTO getWallet(WalletID id);

    List<WalletDTO> getWalletsByWalletOwner(WalletOwnerId walletOwnerId);

    MovementId lockFunds(WalletFundsLockCommand command);
    MovementId lockFunds(WalletOwnerFundsLockCommand command);

    MovementId collectFundLock(WalletFundsLockCollectCommand command);

    MovementId collectFundLock(WalletOwnerFundsLockCollectCommand command);

    Stream<MovementDTO> getMovements(WalletID walletID);

    Stream<MovementDTO> getMovements(WalletID walletID, MovementId startingFrom);

}
