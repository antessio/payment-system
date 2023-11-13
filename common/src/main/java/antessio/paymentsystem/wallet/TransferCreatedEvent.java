package antessio.paymentsystem.wallet;

import antessio.paymentsystem.common.Amount;

public class TransferCreatedEvent {
    private TransferId id;
    private TransferDirection direction;
    private Amount amount;
    private WalletID fromWallet;
    private WalletType fromWalletType;

    public TransferCreatedEvent(
            TransferId id,
            TransferDirection direction,
            Amount amount,
            WalletID fromWallet,
            WalletType fromWalletType) {
        this.id = id;
        this.direction = direction;
        this.amount = amount;
        this.fromWallet = fromWallet;
        this.fromWalletType = fromWalletType;
    }

    public TransferId getId() {
        return id;
    }

    public TransferDirection getDirection() {
        return direction;
    }

    public Amount getAmount() {
        return amount;
    }

    public WalletID getFromWallet() {
        return fromWallet;
    }

    public WalletType getFromWalletType() {
        return fromWalletType;
    }

}
