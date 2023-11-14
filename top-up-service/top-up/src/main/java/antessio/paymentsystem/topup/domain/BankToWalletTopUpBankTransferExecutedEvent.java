package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record BankToWalletTopUpBankTransferExecutedEvent(TopUp.TopUpId topUpId, BankTransfer bankTransfer) {

}
