package antessio.paymentsystem.bank.bankapp.infrastructure.persistence;


import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import antessio.paymentsystem.bank.BankTransferId;
import antessio.paymentsystem.bank.BankTransferStatus;
import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.topup.BankAccountId;
import antessio.paymentystem.bank.domain.BankTransfer;
import antessio.paymentystem.bank.domain.BankTransferRepository;

@Component
public class BankTransferRepositoryAdapter implements BankTransferRepository {

    private final BankTransferSpringDataRepository repository;

    public BankTransferRepositoryAdapter(BankTransferSpringDataRepository repository) {
        this.repository = repository;
    }


    @Override
    public Optional<BankTransfer> loadById(BankTransferId id) {
        return repository.findById(id.id())
                         .map(BankTransferAdapter::new);
    }

    @Override
    public Stream<BankTransfer> loadByBankAccountId(BankAccountId bankAccountId, BankAccountId startingFrom) {
        List<BankTransferEntity> firstPage = repository.findByBankAccountId(bankAccountId.getId(), null, 20);

        return Stream.iterate(
                             firstPage,
                             l -> !l.isEmpty(),
                             l -> repository.findByBankAccountId(bankAccountId.getId(), l.get(l.size() - 1).getId(), 20))
                     .flatMap(Collection::stream)
                     .map(BankTransferAdapter::new);
    }

    @Override
    public void save(BankTransfer bankTransfer) {

    }

    private static class BankTransferAdapter extends BankTransfer {

        private BankTransferStatus status;

        private LocalDate executionDate;

        private LocalDate reversalDate;

        public BankTransferAdapter(BankTransferEntity entity) {
            super(new Amount(entity.getAmountUnit(), entity.getCurrency()), new BankAccountId(entity.getBankAccountId()));
            this.status = entity.getStatus();
            this.executionDate = entity.getExecutionDate();
            this.reversalDate = entity.getReversalDate();
        }

        @Override
        public BankTransferStatus getStatus() {
            return status;
        }

        @Override
        public Optional<LocalDate> getExecutionDate() {
            return Optional.ofNullable(executionDate);
        }

        @Override
        public Optional<LocalDate> getReversalDate() {
            return Optional.ofNullable(reversalDate);
        }

    }

}
