package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.api.wallet.MovementDTO;

public class MovementDTOAdapter extends MovementDTO{

    public MovementDTOAdapter(Movement movement) {
        super(movement.getId(), movement.getDirection(), movement.getAmount(), movement.getWalletId(), movement.getWalletType());

    }



}
