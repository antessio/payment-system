package antessio.paymentsystem.topup.domain;

import antessio.paymentsystem.common.Amount;

public interface WalletTransferService {

    WalletTransfer createWalletTransfer(Wallet wallet, Amount amount);

    void confirmWalletTransfer(WalletTransfer.WalletTransferId id);

    void cancelWalletTransfer(WalletTransfer.WalletTransferId id);

}
