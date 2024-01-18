package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.wallet.TransferCreatedEvent;

public class TransferCreatedEventAdapter extends TransferCreatedEvent {

    public TransferCreatedEventAdapter(Movement movement) {
        super(
                movement.getId(),
                movement.getDirection(),
                movement.getAmount(),
                movement.getWalletId(),
                movement.getWalletType());
    }



}
