package antessio.paymentsystem.wallet;

public class WalletEventAdapter extends WalletEvent{

    public WalletEventAdapter(Wallet wallet) {
        super(wallet.getId(), wallet.getOwner(), wallet.getType(), wallet.getAmountUnit());
    }

}
