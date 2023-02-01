package antessio.paymentsystem.api.wallet;

import java.util.List;
import java.util.stream.Stream;

import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;


public interface WalletService {

    WalletDTO getWallet(WalletID id);

    MovementId lockFunds(LockFundsCommand command);

    List<MovementDTO> moveMoneyFromFundLock(MoveMoneyFromFundLockCommand command);

    Stream<MovementDTO> getMovements(WalletID walletID);

}
