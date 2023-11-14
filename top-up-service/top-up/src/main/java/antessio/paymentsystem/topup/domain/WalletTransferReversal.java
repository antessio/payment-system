package antessio.paymentsystem.topup.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record WalletTransferReversal(antessio.paymentsystem.topup.domain.WalletTransferReversal.WalletTransferReversalId id) {

    public static class WalletTransferReversalId {

        private String id;

        public WalletTransferReversalId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

    }

}
