package antessio.paymentsystem.api.wallet;

import java.util.List;
import java.util.stream.Stream;

import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwnerId;


public interface WalletService {

    WalletDTO getWallet(WalletID id);

    List<WalletDTO> getWalletsByWalletOwner(WalletOwnerId walletOwnerId);

    TransferId lockFunds(WalletFundsLockCommand command);
    TransferId lockFunds(WalletOwnerFundsLockCommand command);

    TransferId collectFundLock(WalletFundsLockCollectCommand command);

    TransferId collectFundLock(WalletOwnerFundsLockCollectCommand command);

    Stream<TransferDTO> getTransfers(WalletID walletID);

    Stream<TransferDTO> getTransfers(WalletID walletID, TransferId startingFrom);

}
