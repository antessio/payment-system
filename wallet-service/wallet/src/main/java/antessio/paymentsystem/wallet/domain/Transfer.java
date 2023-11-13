package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.wallet.TransferDirection;
import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletType;

/**
 *
 */
public class Transfer {
    private TransferId id;
    private TransferDirection direction;
    private Amount amount;
    private WalletID walletId;
    private WalletType walletType;
    private String operationId;

    Transfer(TransferId id, TransferDirection direction, Amount amount, WalletID walletId, WalletType walletType, String operationId) {
        this.id = id;
        this.direction = direction;
        this.amount = amount;
        this.walletId = walletId;
        this.walletType = walletType;
        this.operationId = operationId;
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
