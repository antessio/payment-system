package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.wallet.Amount;
import antessio.paymentsystem.wallet.MovementDirection;
import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletType;

public final class MovementBuilder {

    private MovementId id;
    private MovementDirection direction;
    private Amount amount;
    private WalletID walletId;
    private WalletType walletType;

    private MovementBuilder() {
    }

    public static MovementBuilder aMovement() {
        return new MovementBuilder();
    }

    public MovementBuilder withId(MovementId id) {
        this.id = id;
        return this;
    }

    public MovementBuilder withDirection(MovementDirection direction) {
        this.direction = direction;
        return this;
    }

    public MovementBuilder withAmount(Amount amount) {
        this.amount = amount;
        return this;
    }

    public MovementBuilder withWalletId(WalletID walletId) {
        this.walletId = walletId;
        return this;
    }

    public MovementBuilder withWalletType(WalletType walletType) {
        this.walletType = walletType;
        return this;
    }

    public Movement build() {
        return new Movement(id, direction, amount, walletId, walletType);
    }

}
