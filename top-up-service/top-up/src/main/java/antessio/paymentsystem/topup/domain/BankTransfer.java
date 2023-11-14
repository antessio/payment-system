package antessio.paymentsystem.topup.domain;

import org.jmolecules.ddd.annotation.ValueObject;


@ValueObject
public record BankTransfer(antessio.paymentsystem.topup.domain.BankTransfer.BankTransferId id,
                           antessio.paymentsystem.topup.domain.BankTransfer.BankTransferStatus status) {

    public static class BankTransferId {

        private String id;

        public BankTransferId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

    }

    public enum BankTransferStatus {
        REQUESTED,
        EXECUTED,
        REVERSED,
        FAILED

    }

}
