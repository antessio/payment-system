package antessio.paymentsystem.wallet.domain;

import antessio.paymentsystem.api.wallet.TransferDTO;

public class TransferDTOAdapter extends TransferDTO {

    public TransferDTOAdapter(Transfer transfer) {
        super(transfer.getId(), transfer.getDirection(), transfer.getAmount(), transfer.getWalletId(), transfer.getWalletType());

    }



}
