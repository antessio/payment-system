package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.wallet.MovementDirection;
import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletType;

/**
 *
 */
public class Movement {
    private MovementId id;
    private MovementDirection direction;
    private Amount amount;
    private WalletID walletId;
    private WalletType walletType;
    private String operationId;

    Movement(MovementId id, MovementDirection direction, Amount amount, WalletID walletId, WalletType walletType, String operationId) {
        this.id = id;
        this.direction = direction;
        this.amount = amount;
        this.walletId = walletId;
        this.walletType = walletType;
        this.operationId = operationId;
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

    public WalletID getWalletId() {
        return walletId;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public String getOperationId() {
        return operationId;
    }

}
