package antessio.paymentsystem.api.wallet;

import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;

public final class CollectFundLockCommand {
    private final MovementId fundLockId;
    private final WalletID toWallet;

    private CollectFundLockCommand(MovementId fundLockId, WalletID toWallet) {
        this.fundLockId = fundLockId;
        this.toWallet = toWallet;
    }

    public static CollectFundLockCommand of(MovementId fundLockId, WalletID toWallet) {
        return new CollectFundLockCommand(fundLockId, toWallet);
    }

    public MovementId getFundLockId() {
        return this.fundLockId;
    }

    public WalletID getToWallet() {
        return this.toWallet;
    }

    public CollectFundLockCommand withFundLockId(MovementId fundLockId) {
        return of(fundLockId, getToWallet());
    }

    public CollectFundLockCommand withToWallet(WalletID toWallet) {
        return of(getFundLockId(), toWallet);
    }

}
