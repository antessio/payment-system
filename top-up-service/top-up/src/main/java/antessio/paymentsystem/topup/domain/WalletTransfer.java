package antessio.paymentsystem.topup.domain;

public class WalletTransfer {
    private WalletTransferId id;
    private WalletTransferStatus status;


    public WalletTransfer(WalletTransferId id, WalletTransferStatus status) {
        this.id = id;
        this.status = status;
    }

    public WalletTransferId getId() {
        return id;
    }

    public WalletTransferStatus getStatus() {
        return status;
    }

    public static class WalletTransferId {
        private String id;

        public WalletTransferId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

    }

    public enum WalletTransferStatus{
        PENDING,
        CANCELED,
        COMPLETED,
        REVERSED
    }

}
