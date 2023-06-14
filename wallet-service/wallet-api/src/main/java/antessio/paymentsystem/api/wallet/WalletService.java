package antessio.paymentsystem.api.wallet;

import java.util.stream.Stream;

import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletID;


public interface WalletService {

    WalletDTO getWallet(WalletID id);

    TransferId lockFunds(WalletFundsLockCommand command);
    TransferId lockFunds(WalletOwnerFundsLockCommand command);

    TransferId collectFundLock(WalletFundsLockCollectCommand command);

    TransferId collectFundLock(WalletOwnerFundsLockCollectCommand command);

    Stream<TransferDTO> getMovements(WalletID walletID);

}
