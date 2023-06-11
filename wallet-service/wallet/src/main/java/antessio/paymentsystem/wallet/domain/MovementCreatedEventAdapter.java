package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.wallet.MovementCreatedEvent;

public class MovementCreatedEventAdapter extends MovementCreatedEvent {

    public MovementCreatedEventAdapter(Movement movement) {
        super(movement.getId(),
              movement.getDirection(),
              movement.getAmount(),
              movement.getWalletId(),
              movement.getWalletType());
    }



}
