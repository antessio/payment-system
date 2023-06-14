package antessio.paymentsystem.wallet.domain;

public final class WalletsUpdateBuilder {

    private Wallet sourceWallet;
    private Transfer sourceWalletTransfer;
    private Wallet destinationWallet;
    private Transfer destinationWalletTransfer;

    private WalletsUpdateBuilder() {
    }

    public static WalletsUpdateBuilder aWalletsUpdate() {
        return new WalletsUpdateBuilder();
    }

    public WalletsUpdateBuilder withSourceWallet(Wallet sourceWallet) {
        this.sourceWallet = sourceWallet;
        return this;
    }

    public WalletsUpdateBuilder withSourceWalletMovement(Transfer sourceWalletTransfer) {
        this.sourceWalletTransfer = sourceWalletTransfer;
        return this;
    }

    public WalletsUpdateBuilder withDestinationWallet(Wallet destinationWallet) {
        this.destinationWallet = destinationWallet;
        return this;
    }

    public WalletsUpdateBuilder withDestinationWalletMovement(Transfer destinationWalletTransfer) {
        this.destinationWalletTransfer = destinationWalletTransfer;
        return this;
    }

    public WalletsUpdate build() {
        return new WalletsUpdate(sourceWallet, sourceWalletTransfer, destinationWallet, destinationWalletTransfer);
    }

}
