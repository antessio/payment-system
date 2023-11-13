package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public class BankToWalletTopUpBankTransferExecutedEvent {
    private TopUp.TopUpId topUpId;
    private BankTransfer bankTransfer;

    public BankToWalletTopUpBankTransferExecutedEvent(TopUp.TopUpId topUpId, BankTransfer bankTransfer) {
        this.topUpId = topUpId;
        this.bankTransfer = bankTransfer;
    }

    public TopUp.TopUpId getTopUpId() {
        return topUpId;
    }

    public BankTransfer getBankTransfer() {
        return bankTransfer;
    }

}
