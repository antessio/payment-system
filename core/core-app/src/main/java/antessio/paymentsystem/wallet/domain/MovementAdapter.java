package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.wallet.MovementDirection;
import antessio.paymentsystem.wallet.MovementId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletType;
import antessio.paymentsystem.wallet.infrastructure.persistence.MovementEntity;

public class MovementAdapter extends Movement {

    public MovementAdapter(MovementEntity entity) {
        super(
                new MovementId(entity.getId()),
                MovementDirection.valueOf(entity.getDirection()),
                new Amount(entity.getAmountUnit(), entity.getAmountCurrency()),
                new WalletID(entity.getWallet().getId()),
                WalletType.valueOf(entity.getWallet().getType()),
                entity.getOperationId()
        );
    }

}
