package antessio.paymentsystem.wallet;


public class WalletUpdatedEvent {
    private WalletID id;
    private Long amountUnit;

    public WalletUpdatedEvent(WalletID id, Long amountUnit) {
        this.id = id;
        this.amountUnit = amountUnit;
    }

    public WalletID getId() {
        return id;
    }

    public Long getAmountUnit() {
        return amountUnit;
    }

}
