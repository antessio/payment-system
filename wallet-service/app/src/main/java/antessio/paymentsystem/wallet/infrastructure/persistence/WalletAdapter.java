package antessio.paymentsystem.wallet.infrastructure.persistence;

import java.util.Date;
import java.util.Optional;

import antessio.paymentsystem.wallet.domain.Wallet;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwner;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.WalletType;

public class WalletAdapter extends Wallet {

    public WalletAdapter(WalletEntity entity) {
        super(
                new WalletID(entity.getId()),
                WalletOwner.valueOf(entity.getOwner()),
                new WalletOwnerId(entity.getOwnerId()),
                WalletType.valueOf(entity.getType()),
                entity.getAmountUnit(),
                entity.getInsertDate().toInstant(),
                Optional.ofNullable(entity.getUpdateDate())
                        .map(Date::toInstant)
                        .orElse(null),
                entity.getVersion());
    }

}
