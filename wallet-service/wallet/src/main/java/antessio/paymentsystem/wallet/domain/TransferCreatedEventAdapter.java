package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.wallet.TransferCreatedEvent;

public class TransferCreatedEventAdapter extends TransferCreatedEvent {

    public TransferCreatedEventAdapter(Transfer transfer) {
        super(
                transfer.getId(),
                transfer.getDirection(),
                transfer.getAmount(),
                transfer.getWalletId(),
                transfer.getWalletType());
    }



}
