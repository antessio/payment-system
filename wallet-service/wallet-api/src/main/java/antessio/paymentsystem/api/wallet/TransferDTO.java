package antessio.paymentsystem.api.wallet;


import antessio.paymentsystem.wallet.Amount;
import antessio.paymentsystem.wallet.TransferDirection;
import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletType;

public class TransferDTO {

    private TransferId id;
    private TransferDirection direction;
    private Amount amount;
    private WalletID walletID;
    private WalletType walletType;


    public TransferDTO(TransferId id, TransferDirection direction, Amount amount, WalletID walletID, WalletType walletType) {
        this.id = id;
        this.direction = direction;
        this.amount = amount;
        this.walletID = walletID;
        this.walletType = walletType;
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

    public WalletID getWalletID() {
        return walletID;
    }

    public WalletType getWalletType() {
        return walletType;
    }

}
