package antessio.paymentsystem.topup.domain;

public class WalletTransferReversal {
    private WalletTransferReversalId id;

    public WalletTransferReversal(WalletTransferReversalId id) {
        this.id = id;
    }

    public WalletTransferReversalId getId() {
        return id;
    }

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
