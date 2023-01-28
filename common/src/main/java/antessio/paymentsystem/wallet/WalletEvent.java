package antessio.paymentsystem.wallet;

import java.util.List;


public class WalletEvent {
    private WalletID id;
    private WalletOwner owner;
    private WalletType type;
    private Long amountUnit;

    public WalletEvent(WalletID id, WalletOwner owner, WalletType type, Long amountUnit) {
        this.id = id;
        this.owner = owner;
        this.type = type;
        this.amountUnit = amountUnit;
    }

    public WalletID getId() {
        return id;
    }

    public WalletOwner getOwner() {
        return owner;
    }

    public WalletType getType() {
        return type;
    }

    public Long getAmountUnit() {
        return amountUnit;
    }

}
