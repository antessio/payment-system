package antessio.paymentsystem.wallet.domain;

public class WalletsUpdate {
    private Wallet sourceWallet;
    private Transfer sourceWalletTransfer;


    private Wallet destinationWallet;
    private Transfer destinationWalletTransfer;

    WalletsUpdate(Wallet sourceWallet,
                  Transfer sourceWalletTransfer,
                  Wallet destinationWallet,
                  Transfer destinationWalletTransfer) {
        this.sourceWallet = sourceWallet;
        this.sourceWalletTransfer = sourceWalletTransfer;
        this.destinationWallet = destinationWallet;
        this.destinationWalletTransfer = destinationWalletTransfer;
    }

    public Wallet getSourceWallet() {
        return sourceWallet;
    }

    public Transfer getSourceWalletMovement() {
        return sourceWalletTransfer;
    }

    public Wallet getDestinationWallet() {
        return destinationWallet;
    }

    public Transfer getDestinationWalletMovement() {
        return destinationWalletTransfer;
    }

}
