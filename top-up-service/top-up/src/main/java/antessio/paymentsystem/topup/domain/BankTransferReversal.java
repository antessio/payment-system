package antessio.paymentsystem.topup.domain;

public class BankTransferReversal {
    private BankTransferReversalId id;

    public BankTransferReversal(BankTransferReversalId id) {
        this.id = id;
    }

    public BankTransferReversalId getId() {
        return id;
    }

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
