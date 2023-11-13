package antessio.paymentsystem.topup.domain;

import java.util.Optional;

import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface TopUpRepository {

    void save(TopUp topUp);

    Optional<TopUp> loadById(TopUp.TopUpId topUpId);

}
