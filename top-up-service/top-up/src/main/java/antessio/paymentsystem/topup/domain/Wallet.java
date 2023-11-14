package antessio.paymentsystem.topup.domain;


import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Wallet(antessio.paymentsystem.topup.domain.Wallet.WalletId walletId) {

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
