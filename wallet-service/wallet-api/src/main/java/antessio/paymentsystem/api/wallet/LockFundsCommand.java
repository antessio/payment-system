package antessio.paymentsystem.api.wallet;

import antessio.paymentsystem.wallet.Amount;
import antessio.paymentsystem.wallet.WalletID;

public class LockFundsCommand {
    private Amount amount;
    private WalletID walletID;

    public LockFundsCommand(Amount amount, WalletID walletID) {
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
