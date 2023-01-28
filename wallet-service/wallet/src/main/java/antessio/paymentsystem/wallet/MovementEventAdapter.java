package antessio.paymentsystem.wallet;

public class MovementEventAdapter extends MovementEvent {

    public MovementEventAdapter(Movement movement) {
        super(movement.getId(),
              movement.getDirection(),
              movement.getAmount(),
              movement.getFromWallet(),
              movement.getFromWalletType(),
              movement.getToWallet(),
              movement.getToWalletType());
    }



}
