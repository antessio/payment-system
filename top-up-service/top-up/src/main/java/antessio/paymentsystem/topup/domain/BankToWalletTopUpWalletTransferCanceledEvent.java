package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

import antessio.paymentsystem.topup.TopUpId;

@DomainEvent
public class BankToWalletTopUpWalletTransferCanceledEvent {

    private final TopUpId id;
    private final WalletTransfer walletTransfer;

    public BankToWalletTopUpWalletTransferCanceledEvent(TopUpId id, WalletTransfer walletTransfer) {
        this.id = id;
        this.walletTransfer = walletTransfer;
    }

    public TopUpId getId() {
        return id;
    }

    public WalletTransfer getWalletTransfer() {
        return walletTransfer;
    }

}
