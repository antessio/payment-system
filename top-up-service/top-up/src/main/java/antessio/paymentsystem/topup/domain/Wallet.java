package antessio.paymentsystem.topup.domain;


import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public class Wallet {
    private WalletId walletId;

    public Wallet(WalletId walletId) {
        this.walletId = walletId;
    }

    public WalletId getWalletId() {
        return walletId;
    }

    public static class WalletId {
        private String id;

        public WalletId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

    }

}
