package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public class BankToWalletTopUpWalletTransferCanceledEvent {

    private final TopUp.TopUpId id;
    private final WalletTransfer walletTransfer;

    public BankToWalletTopUpWalletTransferCanceledEvent(TopUp.TopUpId id, WalletTransfer walletTransfer) {
        this.id = id;
        this.walletTransfer = walletTransfer;
    }

    public TopUp.TopUpId getId() {
        return id;
    }

    public WalletTransfer getWalletTransfer() {
        return walletTransfer;
    }

}
