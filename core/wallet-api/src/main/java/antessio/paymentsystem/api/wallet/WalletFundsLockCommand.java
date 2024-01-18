package antessio.paymentsystem.api.wallet;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.wallet.WalletID;

public class WalletFundsLockCommand {
    private Amount amount;
    private WalletID walletID;

    public WalletFundsLockCommand(Amount amount, WalletID walletID) {
        this.amount = amount;
        this.walletID = walletID;
    }

    public Amount getAmount() {
        return amount;
    }

    public WalletID getWalletID() {
        return walletID;
    }

}
