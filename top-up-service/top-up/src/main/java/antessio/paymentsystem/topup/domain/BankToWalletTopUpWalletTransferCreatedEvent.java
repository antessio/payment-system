package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public class BankToWalletTopUpWalletTransferCreatedEvent {
    private TopUp.TopUpId topUpId;
    private WalletTransfer.WalletTransferId walletTransferId;

    public BankToWalletTopUpWalletTransferCreatedEvent(TopUp.TopUpId topUpId, WalletTransfer.WalletTransferId walletTransferId) {
        this.topUpId = topUpId;
        this.walletTransferId = walletTransferId;
    }

    public TopUp.TopUpId getTopUpId() {
        return topUpId;
    }

    public WalletTransfer.WalletTransferId getWalletTransferId() {
        return walletTransferId;
    }

}
