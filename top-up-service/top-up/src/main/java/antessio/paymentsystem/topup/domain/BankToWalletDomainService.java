package antessio.paymentsystem.topup.domain;

import org.jmolecules.ddd.annotation.Service;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.common.Message;
import antessio.paymentsystem.common.MessageBroker;
import antessio.paymentsystem.common.SerializationService;

@Service
public class BankToWalletDomainService {

    private final TopUpRepository topUpRepository;
    private final SerializationService serializationService;
    private final MessageBroker messageBroker;

    public BankToWalletDomainService(SerializationService serializationService, MessageBroker messageBroker, TopUpRepository topUpRepository) {
        this.messageBroker = messageBroker;
        this.topUpRepository = topUpRepository;
        this.serializationService = serializationService;
    }

    public BankToWalletTopUp create(
            BankAccount bankAccount,
            Wallet wallet,
            Amount amount) {
        BankToWalletTopUp bankToWalletTopUp = new BankToWalletTopUp(
                TopUp.TopUpId.generate(),
                amount,
                wallet,
                bankAccount);
        save(bankToWalletTopUp);
        publish(new BankToWalletTopUpRequestedEvent(
                bankToWalletTopUp.getId(),
                bankToWalletTopUp.getAmount(),
                bankToWalletTopUp.getWallet(),
                bankToWalletTopUp.getBankAccount()), "top-up-created");
        return bankToWalletTopUp;
    }

    public void bankTransferCreated(BankToWalletTopUp topUp, BankTransfer.BankTransferId bankTransferId) {
        topUp.markBankTransferCreated(bankTransferId);
        save(topUp);
        publish(new BankToWalletTopUpBankTransferCreatedEvent(topUp.getId(), bankTransferId), "top-up-bank-transfer-created");
    }

    public void walletTransferCreated(BankToWalletTopUp topUp, WalletTransfer.WalletTransferId walletTransferId) {
        topUp.markWalletTransferCreated(walletTransferId);
        save(topUp);
        publish(new BankToWalletTopUpWalletTransferCreatedEvent(topUp.getId(), walletTransferId), "top-up-wallet-transfer-created");
    }

    public void markAsInProgress(
            BankToWalletTopUp bankToWalletTopUp) {
        bankToWalletTopUp.markAsInProgress();
        save(bankToWalletTopUp);
        publish(new TopUpMarkedAsInProgressEvent(bankToWalletTopUp.getId()), "top-up-marked-as-in-progress");
    }

    public void bankTransferExecuted(BankToWalletTopUp bankToWalletTopUp) {

        bankToWalletTopUp.markBankTransferExecuted();
        BankTransfer bankTransferUpdated = bankToWalletTopUp.getBankTransfer()
                                                            .orElseThrow();
        save(bankToWalletTopUp);
        publish(new BankToWalletTopUpBankTransferExecutedEvent(bankToWalletTopUp.getId(), bankTransferUpdated), "top-up-bank-transfer-executed");
    }

    public void complete(BankToWalletTopUp bankToWalletTopUp, WalletTransfer.WalletTransferId walletTransferId) {
        bankToWalletTopUp.complete(walletTransferId);
        save(bankToWalletTopUp);
        WalletTransfer updatedWalletTransfer = bankToWalletTopUp.getWalletTransfer().orElseThrow();
        publish(new BankToWalletTopUpCompletedEvent(bankToWalletTopUp.getId(), updatedWalletTransfer), "top-up-completed");
    }

    public void markBankTransferFailed(BankToWalletTopUp bankToWalletTopUp) {
        bankToWalletTopUp.markBankTransferFailed();
        save(bankToWalletTopUp);
        publish(new BankToWalletTopUpCanceledEvent(bankToWalletTopUp.getId(), "bank-transfer-failed"), "top-up-canceled");
    }

    public void markWalletTransferCanceled(BankToWalletTopUp bankToWalletTopUp, WalletTransfer.WalletTransferId walletTransferId) {
        bankToWalletTopUp.markWalletTransferCanceled(walletTransferId);
        save(bankToWalletTopUp);
        WalletTransfer updatedWalletTransfer = bankToWalletTopUp.getWalletTransfer().orElseThrow();
        publish(new BankToWalletTopUpWalletTransferCanceledEvent(bankToWalletTopUp.getId(), updatedWalletTransfer), "top-up-wallet-transfer-canceled");

    }

    private void save(BankToWalletTopUp bankToWalletTopUp) {
        topUpRepository.save(bankToWalletTopUp);
    }

    private void publish(Object event, String eventName) {
        messageBroker.sendMessage(Message.of(eventName, serializationService.serialize(event)));
    }

}
