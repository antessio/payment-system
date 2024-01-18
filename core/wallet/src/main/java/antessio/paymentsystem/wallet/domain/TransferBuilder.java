package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.wallet.MovementDirection;
import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletType;

public final class TransferBuilder {

    private MovementId id;
    private MovementDirection direction;
    private Amount amount;
    private WalletID walletId;
    private WalletType walletType;
    private String operationId;

    private TransferBuilder() {
    }

    public static TransferBuilder aTransfer() {
        return new TransferBuilder();
    }

    public TransferBuilder withId(MovementId id) {
        this.id = id;
        return this;
    }

    public TransferBuilder withDirection(MovementDirection direction) {
        this.direction = direction;
        return this;
    }

    public TransferBuilder withAmount(Amount amount) {
        this.amount = amount;
        return this;
    }

    public TransferBuilder withWalletId(WalletID walletId) {
        this.walletId = walletId;
        return this;
    }

    public TransferBuilder withWalletType(WalletType walletType) {
        this.walletType = walletType;
        return this;
    }

    public TransferBuilder withOperationId(String operationId) {
        this.operationId = operationId;
        return this;
    }

    public Movement build() {
        return new Movement(id, direction, amount, walletId, walletType, operationId);
    }

}
