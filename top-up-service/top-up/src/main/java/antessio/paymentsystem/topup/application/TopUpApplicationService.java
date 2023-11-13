package antessio.paymentsystem.topup.application;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.topup.domain.BankAccount;
import antessio.paymentsystem.topup.domain.BankToWalletDomainService;
import antessio.paymentsystem.topup.domain.BankToWalletTopUp;
import antessio.paymentsystem.topup.domain.BankTransfer;
import antessio.paymentsystem.topup.domain.BankTransferService;
import antessio.paymentsystem.topup.domain.TopUp;
import antessio.paymentsystem.topup.domain.TopUpRepository;
import antessio.paymentsystem.topup.domain.Wallet;
import antessio.paymentsystem.topup.domain.WalletTransfer;
import antessio.paymentsystem.topup.domain.WalletTransferService;

public class TopUpApplicationService {

    private final TopUpRepository topUpRepository;
    private final BankToWalletDomainService bankToWalletDomainService;
    private final BankTransferService bankTransferService;

    private final WalletTransferService walletTransferService;

    public TopUpApplicationService(
            TopUpRepository topUpRepository,
            BankToWalletDomainService bankToWalletDomainService,
            BankTransferService bankTransferService,
            WalletTransferService walletTransferService) {
        this.topUpRepository = topUpRepository;
        this.bankToWalletDomainService = bankToWalletDomainService;
        this.bankTransferService = bankTransferService;
        this.walletTransferService = walletTransferService;
    }

    public TopUp createBankToWalletTopUp(Amount amount, BankAccount from, Wallet to) {
        return bankToWalletDomainService.create(from, to, amount);
    }

    public void processBankTransfer(TopUp.TopUpId topUpId) {
        TopUp topUp = topUpRepository.loadById(topUpId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with id %s doesn't exist".formatted(topUpId.getId().toString())));
        if (topUp instanceof BankToWalletTopUp bankToWalletTopUp) {
            BankTransfer bankTransfer = bankTransferService.createBankTransfer(
                    bankToWalletTopUp.getBankAccount(),
                    bankToWalletTopUp.getAmount());
            bankToWalletDomainService.bankTransferCreated(
                    bankToWalletTopUp, bankTransfer.getId());
        } else {
            throw new IllegalArgumentException("bank transfer can't be processed for inconsistent top-up type");
        }

    }

    public void processWalletTransfer(TopUp.TopUpId topUpId) {
        TopUp topUp = topUpRepository.loadById(topUpId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with id %s doesn't exist".formatted(topUpId.getId().toString())));
        if (topUp instanceof BankToWalletTopUp bankToWalletTopUp) {
            WalletTransfer walletTransfer = walletTransferService.createWalletTransfer(bankToWalletTopUp.getWallet(), bankToWalletTopUp.getAmount());
            bankToWalletDomainService.walletTransferCreated(
                    bankToWalletTopUp, walletTransfer.getId());
        } else {
            throw new IllegalArgumentException("bank transfer can't be processed for inconsistent top-up type");
        }
    }

    public void markAsInProgress(TopUp.TopUpId topUpId) {
        TopUp topUp = topUpRepository.loadById(topUpId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with id %s doesn't exist".formatted(topUpId.getId().toString())));
        if (topUp instanceof BankToWalletTopUp bankToWalletTopUp) {
            bankToWalletDomainService.markAsInProgress(bankToWalletTopUp);
        } else {
            throw new IllegalArgumentException("bank transfer can't be processed for inconsistent top-up type");
        }

    }

    public void bankTransferExecuted(TopUp.TopUpId topUpId, BankTransfer.BankTransferId bankTransferId) {
        TopUp topUp = topUpRepository.loadById(topUpId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with id %s doesn't exist".formatted(topUpId.getId().toString())));
        if (topUp instanceof BankToWalletTopUp bankToWalletTopUp) {
            bankToWalletDomainService.bankTransferExecuted(bankToWalletTopUp, bankTransferId);
        } else {
            throw new IllegalArgumentException("bank transfer can't be processed for inconsistent top-up type");
        }
    }

    public void confirmWalletTransfer(TopUp.TopUpId topUpId){
        TopUp topUp = topUpRepository.loadById(topUpId)
                                     .orElseThrow(() -> new IllegalArgumentException("top-up with id %s doesn't exist".formatted(topUpId.getId().toString())));
        if (topUp instanceof BankToWalletTopUp bankToWalletTopUp) {
            WalletTransfer walletTransfer = bankToWalletTopUp.getWalletTransfer()
                                                             .orElseThrow(() -> new IllegalStateException("top-up has no wallet transfer"));
            walletTransferService.confirmWalletTransfer(walletTransfer.getId());
            bankToWalletDomainService.walletTransferExecuted(bankToWalletTopUp, walletTransfer.getId());
        } else {
            throw new IllegalArgumentException("bank transfer can't be processed for inconsistent top-up type");
        }
    }

}
