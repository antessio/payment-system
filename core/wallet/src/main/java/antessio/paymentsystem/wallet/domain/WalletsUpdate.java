package antessio.paymentsystem.wallet.domain;

public class WalletsUpdate {
    private Wallet sourceWallet;
    private Movement sourceWalletMovement;


    private Wallet destinationWallet;
    private Movement destinationWalletMovement;

    WalletsUpdate(Wallet sourceWallet,
                  Movement sourceWalletMovement,
                  Wallet destinationWallet,
                  Movement destinationWalletMovement) {
        this.sourceWallet = sourceWallet;
        this.sourceWalletMovement = sourceWalletMovement;
        this.destinationWallet = destinationWallet;
        this.destinationWalletMovement = destinationWalletMovement;
    }

    public Wallet getSourceWallet() {
        return sourceWallet;
    }

    public Movement getSourceWalletMovement() {
        return sourceWalletMovement;
    }

    public Wallet getDestinationWallet() {
        return destinationWallet;
    }

    public Movement getDestinationWalletMovement() {
        return destinationWalletMovement;
    }

}
