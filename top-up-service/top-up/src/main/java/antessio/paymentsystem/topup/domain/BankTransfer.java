package antessio.paymentsystem.topup.domain;

import org.jmolecules.ddd.annotation.ValueObject;


@ValueObject
public class BankTransfer {
    private BankTransferId id;
    private BankTransferStatus status;

    public BankTransfer(BankTransferId id,
                        BankTransferStatus status) {
        this.id = id;
        this.status = status;
    }

    public BankTransferId getId() {
        return id;
    }

    public BankTransferStatus getStatus() {
        return status;
    }

    public static class BankTransferId {

        private String id;

        public BankTransferId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

    }

    public enum BankTransferStatus{
        REQUESTED,
        EXECUTED,
        REVERSED,
        CANCELED

    }

}
