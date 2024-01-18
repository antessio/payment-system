package antessio.paymentsystem.wallet.domain;

public final class WalletsUpdateBuilder {

    private Wallet sourceWallet;
    private Movement sourceWalletMovement;
    private Wallet destinationWallet;
    private Movement destinationWalletMovement;

    private WalletsUpdateBuilder() {
    }

    public static WalletsUpdateBuilder aWalletsUpdate() {
        return new WalletsUpdateBuilder();
    }

    public WalletsUpdateBuilder withSourceWallet(Wallet sourceWallet) {
        this.sourceWallet = sourceWallet;
        return this;
    }

    public WalletsUpdateBuilder withSourceWalletMovement(Movement sourceWalletMovement) {
        this.sourceWalletMovement = sourceWalletMovement;
        return this;
    }

    public WalletsUpdateBuilder withDestinationWallet(Wallet destinationWallet) {
        this.destinationWallet = destinationWallet;
        return this;
    }

    public WalletsUpdateBuilder withDestinationWalletMovement(Movement destinationWalletMovement) {
        this.destinationWalletMovement = destinationWalletMovement;
        return this;
    }

    public WalletsUpdate build() {
        return new WalletsUpdate(sourceWallet, sourceWalletMovement, destinationWallet, destinationWalletMovement);
    }

}
