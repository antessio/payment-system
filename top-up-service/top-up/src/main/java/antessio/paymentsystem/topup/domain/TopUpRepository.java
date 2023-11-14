package antessio.paymentsystem.topup.domain;

import java.util.Optional;

import org.jmolecules.ddd.annotation.Repository;

import antessio.paymentsystem.topup.TopUpId;

@Repository
public interface TopUpRepository {

    void save(TopUp topUp);

    Optional<TopUp> loadById(TopUpId topUpId);

    Optional<TopUp> loadTopUpByBankTransferId(BankTransfer.BankTransferId bankTransferId);

    Optional<TopUp> loadByWalletTransferId(WalletTransfer.WalletTransferId walletTransferId);

}
