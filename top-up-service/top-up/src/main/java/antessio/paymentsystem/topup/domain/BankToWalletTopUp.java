package antessio.paymentsystem.topup.domain;

import java.util.Optional;

import org.jmolecules.ddd.annotation.AggregateRoot;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.topup.TopUpId;

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


    /**
     * Links a bank transfer requested to this top-up
     * @param bankTransferId
     */
    void markBankTransferCreated(BankTransfer.BankTransferId bankTransferId) {
        if (getBankTransfer().isPresent()) {
            throw new IllegalArgumentException("bank transfer already created");
        }
        this.bankTransfer = new BankTransfer(bankTransferId, BankTransfer.BankTransferStatus.REQUESTED);
    }


    /**
     * Links a wallet transfer pending to this top-up
     * @param walletTransferId
     */
    void markWalletTransferCreated(WalletTransfer.WalletTransferId walletTransferId) {
        if (getWalletTransfer().isPresent()) {
            throw new IllegalArgumentException("wallet transfer already created");
        }
        this.walletTransfer = new WalletTransfer(walletTransferId, WalletTransfer.WalletTransferStatus.PENDING);
    }

    /**
     * Marks the bank account linked to this top-up as executed
     */
    void markBankTransferExecuted() {
        updateBankTransferStatus(BankTransfer.BankTransferStatus.EXECUTED);
    }

    /**
     * Completes this top-up (updates the status) and mark the wallet transfer as completed
     * @param walletTransferId
     */
    void complete(WalletTransfer.WalletTransferId walletTransferId) {
        updateWalletTransferStatus(walletTransferId, WalletTransfer.WalletTransferStatus.COMPLETED);
        complete();
    }

    /**
     * Marks the bank transfer as failed and cancels this top-up (updates the status)
     */
    public void markBankTransferFailed() {
        updateBankTransferStatus(BankTransfer.BankTransferStatus.FAILED);
        cancel();
    }

    public void markWalletTransferCanceled(WalletTransfer.WalletTransferId walletTransferId) {
        updateWalletTransferStatus(walletTransferId, WalletTransfer.WalletTransferStatus.CANCELED);
    }

    private void updateBankTransferStatus(BankTransfer.BankTransferId bankTransferId, BankTransfer.BankTransferStatus newBankTransferStatus) {
        if (getBankTransfer().filter(bt -> bt.id().equals(bankTransferId)).isEmpty()) {
            throw new IllegalArgumentException("bank transfer is different from expected for this top-up");
        }
        this.bankTransfer = new BankTransfer(this.bankTransfer.id(), newBankTransferStatus);
    }
    private void updateBankTransferStatus(BankTransfer.BankTransferStatus newBankTransferStatus) {
        BankTransfer.BankTransferId bankTransferId = getBankTransfer()
                .map(BankTransfer::id)
                .orElseThrow(() -> new IllegalArgumentException("bank transfer is empty for this top-up"));
        updateBankTransferStatus(bankTransferId, newBankTransferStatus);
    }

    private void updateWalletTransferStatus(WalletTransfer.WalletTransferId walletTransferId, WalletTransfer.WalletTransferStatus newWalletTransferStatus) {
        if (getWalletTransfer().isEmpty()) {
            throw new IllegalArgumentException("wallet transfer is empty for this top-up");
        }
        if (getWalletTransfer().filter(bt -> bt.id().equals(walletTransferId)).isEmpty()) {
            throw new IllegalArgumentException("wallet transfer is different from expected for this top-up");
        }
        this.walletTransfer = new WalletTransfer(this.walletTransfer.id(), newWalletTransferStatus);
    }

}
