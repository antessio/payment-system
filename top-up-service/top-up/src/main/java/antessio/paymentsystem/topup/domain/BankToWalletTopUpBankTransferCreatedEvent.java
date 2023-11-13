package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public class BankToWalletTopUpBankTransferCreatedEvent{
    private TopUp.TopUpId topUpId;
    private BankTransfer.BankTransferId bankTransferId;

    public BankToWalletTopUpBankTransferCreatedEvent(TopUp.TopUpId topUpId, BankTransfer.BankTransferId bankTransferId) {
        this.topUpId = topUpId;
        this.bankTransferId = bankTransferId;
    }

    public TopUp.TopUpId getTopUpId() {
        return topUpId;
    }

    public BankTransfer.BankTransferId getBankTransferId() {
        return bankTransferId;
    }

}
