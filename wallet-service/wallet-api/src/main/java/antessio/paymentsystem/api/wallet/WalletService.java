package antessio.paymentsystem.api.wallet;

import java.util.stream.Stream;

import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;


public interface WalletService {

    WalletDTO getWallet(WalletID id);

    MovementId lockFunds(LockFundsCommand command);

    MovementId collectFundLock(CollectFundLockCommand command);

    Stream<MovementDTO> getMovements(WalletID walletID);

}
