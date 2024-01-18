package antessio.paymentsystem.topup.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record WalletTransfer(antessio.paymentsystem.topup.domain.WalletTransfer.WalletTransferId id,
                             antessio.paymentsystem.topup.domain.WalletTransfer.WalletTransferStatus status) {

    public static class WalletTransferId {

        private String id;

        public WalletTransferId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

    }

    public enum WalletTransferStatus {
        PENDING,
        CANCELED,
        COMPLETED,
        REVERSED
    }

}
