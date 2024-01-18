package antessio.paymentsystem.api.wallet;

import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwner;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.WalletType;


public class WalletDTO {
    private WalletID id;
    private WalletOwner owner;
    private WalletOwnerId ownerId;
    private WalletType type;
    private Long amountUnit;

    public WalletDTO(WalletID id, WalletOwner owner, WalletOwnerId ownerId, WalletType type, Long amountUnit) {
        this.id = id;
        this.owner = owner;
        this.ownerId = ownerId;
        this.type = type;
        this.amountUnit = amountUnit;
    }

    public WalletID getId() {
        return id;
    }

    public WalletOwner getOwner() {
        return owner;
    }

    public WalletOwnerId getOwnerId() {
        return ownerId;
    }

    public WalletType getType() {
        return type;
    }

    public Long getAmountUnit() {
        return amountUnit;
    }

}
