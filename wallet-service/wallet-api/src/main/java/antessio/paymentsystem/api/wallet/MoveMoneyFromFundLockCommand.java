package antessio.paymentsystem.api.wallet;

import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;

public final class MoveMoneyFromFundLockCommand {
    private final MovementId fundLockId;
    private final WalletID toWallet;

    private MoveMoneyFromFundLockCommand(MovementId fundLockId, WalletID toWallet) {
        this.fundLockId = fundLockId;
        this.toWallet = toWallet;
    }

    public static MoveMoneyFromFundLockCommand of(MovementId fundLockId, WalletID toWallet) {
        return new MoveMoneyFromFundLockCommand(fundLockId, toWallet);
    }

    public MovementId getFundLockId() {
        return this.fundLockId;
    }

    public WalletID getToWallet() {
        return this.toWallet;
    }

    public MoveMoneyFromFundLockCommand withFundLockId(MovementId fundLockId) {
        return of(fundLockId, getToWallet());
    }

    public MoveMoneyFromFundLockCommand withToWallet(WalletID toWallet) {
        return of(getFundLockId(), toWallet);
    }

}
