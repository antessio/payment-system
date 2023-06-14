package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.wallet.Amount;
import antessio.paymentsystem.wallet.TransferDirection;
import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletType;

public final class TransferBuilder {

    private TransferId id;
    private TransferDirection direction;
    private Amount amount;
    private WalletID walletId;
    private WalletType walletType;
    private String operationId;

    private TransferBuilder() {
    }

    public static TransferBuilder aTransfer() {
        return new TransferBuilder();
    }

    public TransferBuilder withId(TransferId id) {
        this.id = id;
        return this;
    }

    public TransferBuilder withDirection(TransferDirection direction) {
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

    public Transfer build() {
        return new Transfer(id, direction, amount, walletId, walletType, operationId);
    }

}
