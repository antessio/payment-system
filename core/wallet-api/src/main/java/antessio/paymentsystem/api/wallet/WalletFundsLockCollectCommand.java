package antessio.paymentsystem.api.wallet;

import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;

public final class WalletFundsLockCollectCommand {
    private final MovementId fundLockId;
    private final WalletID toWallet;

    private WalletFundsLockCollectCommand(MovementId fundLockId, WalletID toWallet) {
        this.fundLockId = fundLockId;
        this.toWallet = toWallet;
    }

    public static WalletFundsLockCollectCommand of(MovementId fundLockId, WalletID toWallet) {
        return new WalletFundsLockCollectCommand(fundLockId, toWallet);
    }

    public MovementId getFundLockId() {
        return this.fundLockId;
    }

    public WalletID getToWallet() {
        return this.toWallet;
    }

    public WalletFundsLockCollectCommand withFundLockId(MovementId fundLockId) {
        return of(fundLockId, getToWallet());
    }

    public WalletFundsLockCollectCommand withToWallet(WalletID toWallet) {
        return of(getFundLockId(), toWallet);
    }

}
