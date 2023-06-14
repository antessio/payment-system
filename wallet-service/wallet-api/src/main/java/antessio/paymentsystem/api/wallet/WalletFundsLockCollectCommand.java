package antessio.paymentsystem.api.wallet;

import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletID;

public final class WalletFundsLockCollectCommand {
    private final TransferId fundLockId;
    private final WalletID toWallet;

    private WalletFundsLockCollectCommand(TransferId fundLockId, WalletID toWallet) {
        this.fundLockId = fundLockId;
        this.toWallet = toWallet;
    }

    public static WalletFundsLockCollectCommand of(TransferId fundLockId, WalletID toWallet) {
        return new WalletFundsLockCollectCommand(fundLockId, toWallet);
    }

    public TransferId getFundLockId() {
        return this.fundLockId;
    }

    public WalletID getToWallet() {
        return this.toWallet;
    }

    public WalletFundsLockCollectCommand withFundLockId(TransferId fundLockId) {
        return of(fundLockId, getToWallet());
    }

    public WalletFundsLockCollectCommand withToWallet(WalletID toWallet) {
        return of(getFundLockId(), toWallet);
    }

}
