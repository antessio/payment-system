package antessio.paymentystem.bank.application;

import java.util.Optional;
import java.util.stream.Stream;

import antessio.paymentsystem.bank.api.BankAccountDTO;
import antessio.paymentsystem.topup.BankAccountId;
import antessio.paymentystem.bank.domain.BankAccount;
import antessio.paymentystem.bank.domain.BankAccountRepository;

public class BankAccountApplicationService{

    private final BankAccountRepository bankAccountRepository;

    public BankAccountApplicationService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    
    public BankAccountDTO createBankAccount(String iban, String owner) {
        if (bankAccountRepository.loadByIban(iban).isPresent()) {
            throw new IllegalArgumentException("iban already exists");
        }
        BankAccount bankAccount = new BankAccount(iban, owner);
        bankAccountRepository.save(bankAccount);
        return toDTO(bankAccount);
    }

    
    public Optional<BankAccountDTO> loadById(BankAccountId bankAccountId) {
        return bankAccountRepository.loadById(bankAccountId)
                                    .map(BankAccountApplicationService::toDTO);
    }

    
    public Optional<BankAccountDTO> loadByIban(String iban) {
        return bankAccountRepository.loadByIban(iban)
                                    .map(BankAccountApplicationService::toDTO);
    }

    public Stream<BankAccountDTO> loadByOwner(String owner, BankAccountId startingFrom) {
        return bankAccountRepository.loadByOwner(owner, startingFrom)
                                    .map(BankAccountApplicationService::toDTO);
    }

    private static BankAccountDTO toDTO(BankAccount a) {
        return new BankAccountDTO(a.getId(), a.getIban(), a.getOwner());
    }

}
