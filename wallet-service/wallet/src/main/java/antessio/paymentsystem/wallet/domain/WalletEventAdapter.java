package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.wallet.WalletCreatedEvent;

public class WalletEventAdapter extends WalletCreatedEvent {

    public WalletEventAdapter(Wallet wallet) {
        super(wallet.getId(), wallet.getOwner(), wallet.getType(), wallet.getAmountUnit());
    }

}
