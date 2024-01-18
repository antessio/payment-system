package antessio.paymentsystem.topup.domain;


import org.jmolecules.ddd.annotation.ValueObject;

import antessio.paymentsystem.topup.WalletId;

@ValueObject
public record Wallet(WalletId walletId) {

}
