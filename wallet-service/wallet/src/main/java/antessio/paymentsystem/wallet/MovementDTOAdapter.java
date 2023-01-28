package antessio.paymentsystem.wallet;

import antessio.paymentsystem.api.wallet.MovementDTO;

public class MovementDTOAdapter extends MovementDTO {

    public MovementDTOAdapter(Movement movement) {
        super(movement.getId(),
              movement.getDirection(),
              movement.getAmount(),
              movement.getFromWallet(),
              movement.getFromWalletType(),
              movement.getToWallet(),
              movement.getToWalletType());
    }



}
