package antessio.paymentsystem.api.wallet;

import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwnerId;

public final class WalletOwnerFundsLockCollectCommand {
    private final MovementId fundLockId;
    private final WalletOwnerId toWalletOwnerId;

    private WalletOwnerFundsLockCollectCommand(MovementId fundLockId, WalletOwnerId toWalletOwnerId) {
        this.fundLockId = fundLockId;
        this.toWalletOwnerId = toWalletOwnerId;
    }

    public static WalletOwnerFundsLockCollectCommand of(MovementId fundLockId, WalletOwnerId toWallet) {
        return new WalletOwnerFundsLockCollectCommand(fundLockId, toWallet);
    }

    public MovementId getFundLockId() {
        return this.fundLockId;
    }

    public WalletOwnerId getToWalletOwnerId() {
        return this.toWalletOwnerId;
    }

    public WalletOwnerFundsLockCollectCommand withFundLockId(MovementId fundLockId) {
        return of(fundLockId, getToWalletOwnerId());
    }

    public WalletOwnerFundsLockCollectCommand withToWalletOwnerId(WalletOwnerId toWalletOwnerId) {
        return of(getFundLockId(), toWalletOwnerId);
    }

}
