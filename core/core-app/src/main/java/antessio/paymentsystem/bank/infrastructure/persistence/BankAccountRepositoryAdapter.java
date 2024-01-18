package antessio.paymentsystem.bank.infrastructure.persistence;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import antessio.paymentsystem.topup.BankAccountId;
import antessio.paymentystem.bank.domain.BankAccount;
import antessio.paymentystem.bank.domain.BankAccountRepository;

@Component
public class BankAccountRepositoryAdapter implements BankAccountRepository {

    private final BankAccountSpringDataRepository bankAccountSpringDataRepository;

    public BankAccountRepositoryAdapter(BankAccountSpringDataRepository bankAccountSpringDataRepository) {
        this.bankAccountSpringDataRepository = bankAccountSpringDataRepository;
    }

    @Override
    public Optional<BankAccount> loadById(BankAccountId bankAccountId) {

        return bankAccountSpringDataRepository.findById(bankAccountId.getId())
                                              .map(BankAccountEntityAdapter::new);
    }

    @Override
    public void save(BankAccount bankAccount) {
        bankAccountSpringDataRepository.save(toEntity(bankAccount));
    }

    @Override
    public Optional<BankAccount> loadByIban(String iban) {
        return bankAccountSpringDataRepository.findByIban(iban)
                                              .map(BankAccountEntityAdapter::new);
    }

    @Override
    public Stream<BankAccount> loadByOwner(String owner, BankAccountId startingFrom) {
        List<BankAccountEntity> firstPage = bankAccountSpringDataRepository.findByBankAccountByOwnerStartingFrom(owner, null, 20);

        return Stream.iterate(
                             firstPage,
                             l -> !l.isEmpty(),
                             l -> bankAccountSpringDataRepository.findByBankAccountByOwnerStartingFrom(owner, l.get(l.size() - 1).getId(), 20))
                     .flatMap(Collection::stream)
                     .map(BankAccountEntityAdapter::new);

    }

    private static BankAccountEntity toEntity(BankAccount bankAccount) {
        return new BankAccountEntity(
                bankAccount.getId().getId(),
                bankAccount.getIban(),
                bankAccount.getOwner(),
                null,
                null,
                bankAccount.getVersion()
        );
    }

    private static class BankAccountEntityAdapter extends BankAccount {

        private BankAccountId id;

        private String iban;

        private String owner;

        private Date insertDate;

        private Date updateDate;

        private Long version;

        public BankAccountEntityAdapter(BankAccountEntity e) {
            super(e.getIban(), e.getOwner());
            this.id = new BankAccountId(e.getId());
            this.insertDate = e.getInsertDate();
            this.updateDate = e.getUpdateDate();
            this.version = e.getVersion();
        }

        @Override
        public BankAccountId getId() {
            return this.id;
        }

        @Override
        public String getIban() {
            return this.iban;
        }

        @Override
        public String getOwner() {
            return this.owner;
        }

        @Override
        public Long getVersion() {
            return super.getVersion();
        }

    }

}
