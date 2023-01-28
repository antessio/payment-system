package antessio.paymentsystem.wallet;


class MoveMoneyCommand {
    private WalletID fromWallet;
    private WalletID toWallet;
    private Amount amount;

    public MoveMoneyCommand(WalletID fromWallet, WalletID toWallet, Amount amount) {
        this.fromWallet = fromWallet;
        this.toWallet = toWallet;
        this.amount = amount;
    }

    public WalletID getFromWallet() {
        return fromWallet;
    }

    public WalletID getToWallet() {
        return toWallet;
    }

    public Amount getAmount() {
        return amount;
    }

}
