package antessio.paymentsystem.topup.dto;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.topup.BankAccountId;
import antessio.paymentsystem.topup.TopUpId;
import antessio.paymentsystem.topup.TopUpStatus;
import antessio.paymentsystem.topup.WalletId;

public class BankToWalletTopUpCreateDto {
    private Amount amount;
    private BankAccountId bankAccountId;

    private WalletId walletId;

    public BankToWalletTopUpCreateDto() {
    }

    public BankToWalletTopUpCreateDto(Amount amount, BankAccountId bankAccountId, WalletId walletId) {
        this.amount = amount;
        this.bankAccountId = bankAccountId;
        this.walletId = walletId;
    }

    public Amount getAmount() {
        return amount;
    }

    public BankAccountId getBankAccountId() {
        return bankAccountId;
    }

    public WalletId getWalletId() {
        return walletId;
    }

}
