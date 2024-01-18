package antessio.paymentsystem.topup.domain;

import antessio.paymentsystem.common.Amount;

public interface BankTransferService {

    BankTransfer createBankTransfer(BankAccount bankAccount, Amount amount);

}
