package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.api.wallet.WalletDTO;

public class WalletDTOAdapter extends WalletDTO {

    public WalletDTOAdapter(Wallet wallet) {

        super(wallet.getId(), wallet.getOwner(),wallet.getOwnerId(),
              wallet.getType(),wallet.getAmountUnit());
    }

}
