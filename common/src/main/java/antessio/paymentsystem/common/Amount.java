package antessio.paymentsystem.common;

public class Amount {
    private Long amountUnit;
    private String currency;

    public Amount(Long amountUnit, String currency) {
        this.amountUnit = amountUnit;
        this.currency = currency;
    }

    public Long getAmountUnit() {
        return amountUnit;
    }

    public String getCurrency() {
        return currency;
    }

}
