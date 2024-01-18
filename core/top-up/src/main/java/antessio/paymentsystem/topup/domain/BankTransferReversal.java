package antessio.paymentsystem.topup.domain;

public record BankTransferReversal(antessio.paymentsystem.topup.domain.BankTransferReversal.BankTransferReversalId id) {

    public static class BankTransferReversalId {

        private String id;

        public BankTransferReversalId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

    }

}
