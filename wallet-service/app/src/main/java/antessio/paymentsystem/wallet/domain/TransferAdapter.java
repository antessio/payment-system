package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.wallet.TransferDirection;
import antessio.paymentsystem.wallet.TransferId;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletType;
import antessio.paymentsystem.wallet.infrastructure.persistence.TransferEntity;

public class TransferAdapter extends Transfer {

    public TransferAdapter(TransferEntity entity) {
        super(
                new TransferId(entity.getId()),
                TransferDirection.valueOf(entity.getDirection()),
                new Amount(entity.getAmountUnit(), entity.getAmountCurrency()),
                new WalletID(entity.getWallet().getId()),
                WalletType.valueOf(entity.getWallet().getType()),
                entity.getOperationId()
        );
    }

}
