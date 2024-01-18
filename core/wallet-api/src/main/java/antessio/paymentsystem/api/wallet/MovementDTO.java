package antessio.paymentsystem.api.wallet;


import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.wallet.MovementDirection;
import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletType;

public class MovementDTO {

    private MovementId id;
    private MovementDirection direction;
    private Amount amount;
    private WalletID walletID;
    private WalletType walletType;


    public MovementDTO(MovementId id, MovementDirection direction, Amount amount, WalletID walletID, WalletType walletType) {
        this.id = id;
        this.direction = direction;
        this.amount = amount;
        this.walletID = walletID;
        this.walletType = walletType;
    }

    public MovementId getId() {
        return id;
    }

    public MovementDirection getDirection() {
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
