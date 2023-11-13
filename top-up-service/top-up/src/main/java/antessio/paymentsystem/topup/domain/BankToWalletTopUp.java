package antessio.paymentsystem.topup.domain;

import java.util.Optional;

import org.jmolecules.ddd.annotation.AggregateRoot;

import antessio.paymentsystem.common.Amount;

@AggregateRoot
public class BankToWalletTopUp extends TopUp {

    private Wallet wallet;

    private BankAccount bankAccount;

    private BankTransfer bankTransfer;

    private WalletTransfer walletTransfer;

    private BankTransferReversal bankTransferReversal;

    private WalletTransferReversal walletTransferReversal;

    public BankToWalletTopUp(TopUpId id, Amount amount, Wallet wallet, BankAccount bankAccount) {
        super(id, amount);
        this.wallet = wallet;
        this.bankAccount = bankAccount;
    }

    public Optional<BankTransfer> getBankTransfer() {
        return Optional.ofNullable(bankTransfer);
    }

    public Optional<WalletTransfer> getWalletTransfer() {
        return Optional.ofNullable(walletTransfer);
    }

    public Optional<BankTransferReversal> getBankTransferReversal() {
        return Optional.ofNullable(bankTransferReversal);
    }

    public Optional<WalletTransferReversal> getWalletTransferReversal() {
        return Optional.ofNullable(walletTransferReversal);
    }

    public Wallet getWallet() {
        return wallet;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }


    void bankTransferCreated(BankTransfer.BankTransferId bankTransferId) {
        if (getBankTransfer().isPresent()) {
            throw new IllegalArgumentException("bank transfer already created");
        }
        this.bankTransfer = new BankTransfer(bankTransferId, BankTransfer.BankTransferStatus.REQUESTED);
    }


    void walletTransferCreated(WalletTransfer.WalletTransferId walletTransferId) {
        if (getWalletTransfer().isPresent()) {
            throw new IllegalArgumentException("wallet transfer already created");
        }
        this.walletTransfer = new WalletTransfer(walletTransferId, WalletTransfer.WalletTransferStatus.PENDING);
    }

    void bankTransferExecuted(BankTransfer.BankTransferId bankTransferId) {
        updateBankTransferStatus(bankTransferId, BankTransfer.BankTransferStatus.EXECUTED);
    }

    void walletTransferExecuted(WalletTransfer.WalletTransferId walletTransferId) {
        updateWalletTransferStatus(walletTransferId, WalletTransfer.WalletTransferStatus.COMPLETED);
    }

    private void updateBankTransferStatus(BankTransfer.BankTransferId bankTransferId, BankTransfer.BankTransferStatus newBankTransferStatus) {
        if (getBankTransfer().isEmpty()) {
            throw new IllegalArgumentException("bank transfer is empty for this top-up");
        }
        if (getBankTransfer().filter(bt -> bt.getId().equals(bankTransferId)).isEmpty()) {
            throw new IllegalArgumentException("bank transfer is different from expected for this top-up");
        }
        this.bankTransfer = new BankTransfer(this.bankTransfer.getId(), newBankTransferStatus);
    }

    private void updateWalletTransferStatus(WalletTransfer.WalletTransferId walletTransferId, WalletTransfer.WalletTransferStatus newWalletTransferStatus) {
        if (getWalletTransfer().isEmpty()) {
            throw new IllegalArgumentException("wallet transfer is empty for this top-up");
        }
        if (getWalletTransfer().filter(bt -> bt.getId().equals(walletTransferId)).isEmpty()) {
            throw new IllegalArgumentException("wallet transfer is different from expected for this top-up");
        }
        this.walletTransfer = new WalletTransfer(this.walletTransfer.getId(), newWalletTransferStatus);
    }

}
