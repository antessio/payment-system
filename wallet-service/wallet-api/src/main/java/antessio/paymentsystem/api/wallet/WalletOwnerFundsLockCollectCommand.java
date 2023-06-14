package antessio.paymentsystem.api.wallet;

import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletOwnerId;

public final class WalletOwnerFundsLockCollectCommand {
    private final TransferId fundLockId;
    private final WalletOwnerId toWalletOwnerId;

    private WalletOwnerFundsLockCollectCommand(TransferId fundLockId, WalletOwnerId toWalletOwnerId) {
        this.fundLockId = fundLockId;
        this.toWalletOwnerId = toWalletOwnerId;
    }

    public static WalletOwnerFundsLockCollectCommand of(TransferId fundLockId, WalletOwnerId toWallet) {
        return new WalletOwnerFundsLockCollectCommand(fundLockId, toWallet);
    }

    public TransferId getFundLockId() {
        return this.fundLockId;
    }

    public WalletOwnerId getToWalletOwnerId() {
        return this.toWalletOwnerId;
    }

    public WalletOwnerFundsLockCollectCommand withFundLockId(TransferId fundLockId) {
        return of(fundLockId, getToWalletOwnerId());
    }

    public WalletOwnerFundsLockCollectCommand withToWalletOwnerId(WalletOwnerId toWalletOwnerId) {
        return of(getFundLockId(), toWalletOwnerId);
    }

}
