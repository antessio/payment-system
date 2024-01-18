package antessio.paymentsystem.bank.infrastructure.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import antessio.paymentsystem.bank.api.BankAccountApplicationServiceApi;
import antessio.paymentsystem.bank.api.BankAccountDTO;
import antessio.paymentsystem.common.PaginatedList;
import antessio.paymentsystem.common.http.HttpNotFoundException;
import antessio.paymentsystem.topup.BankAccountId;
import antessio.paymentystem.bank.application.BankAccountApplicationService;

@RestController
@RequestMapping("/api/bankAccount")
public class BankAccountController implements BankAccountApplicationServiceApi {

    private final BankAccountApplicationService bankAccountApplicationService;

    public BankAccountController(BankAccountApplicationService bankAccountApplicationService) {
        this.bankAccountApplicationService = bankAccountApplicationService;
    }


    @PostMapping("/")
    @Override
    public BankAccountDTO createBankAccount(String iban, String owner) {
        return bankAccountApplicationService.createBankAccount(iban, owner);
    }

    @GetMapping("/{id}")
    @Override
    public BankAccountDTO loadById(@PathVariable BankAccountId id) {
        return bankAccountApplicationService.loadById(id)
                                            .orElseThrow(() -> new HttpNotFoundException("id %s not found".formatted(id.getId())));

    }

    @GetMapping("/")
    @Override
    public BankAccountDTO loadByIban(@RequestParam("iban") String iban) {
        return bankAccountApplicationService.loadByIban(iban)
                                            .orElseThrow(() -> new HttpNotFoundException("iban %s not found".formatted(iban)));
    }

    @Override
    public PaginatedList<BankAccountDTO> loadByOwner(String owner, Integer limit, BankAccountId startingFrom) {
        List<BankAccountDTO> results = bankAccountApplicationService.loadByOwner(owner, startingFrom)
                                                                    .limit(limit + 1)
                                                                    .collect(Collectors.toList());
        return new PaginatedList<>(results.subList(0, limit), results.size() > limit);
    }


}
