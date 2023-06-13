package antessio.paymentsystem.api.wallet;

import antessio.paymentsystem.wallet.Amount;
import antessio.paymentsystem.wallet.WalletOwnerId;

public class WalletOwnerFundsLockCommand {
    private Amount amount;
    private WalletOwnerId walletOwnerId;

    public WalletOwnerFundsLockCommand(Amount amount, WalletOwnerId walletOwnerId) {
        this.amount = amount;
        this.walletOwnerId = walletOwnerId;
    }

    public Amount getAmount() {
        return amount;
    }

    public WalletOwnerId getWalletOwnerId() {
        return walletOwnerId;
    }

}
