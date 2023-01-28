package antessio.paymentsystem.api.wallet;

import java.util.List;

import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;


public interface WalletService {

    WalletDTO getWallet(WalletID id);

    MovementId lockFunds(LockFundsCommand command);

    List<MovementDTO> moveMoneyFromFundLock(MoveMoneyFromFundLockCommand command);

}
