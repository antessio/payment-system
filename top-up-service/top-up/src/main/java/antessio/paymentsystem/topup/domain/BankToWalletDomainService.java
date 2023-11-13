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
        topUpRepository.save(bankToWalletTopUp);
        publish(new BankToWalletTopUpRequestedEvent(
                bankToWalletTopUp.getId(),
                bankToWalletTopUp.getAmount(),
                bankToWalletTopUp.getWallet(),
                bankToWalletTopUp.getBankAccount()), "top-up-created");
        return bankToWalletTopUp;
    }

    public void bankTransferCreated(BankToWalletTopUp topUp, BankTransfer.BankTransferId bankTransferId) {
        topUp.bankTransferCreated(bankTransferId);
        topUpRepository.save(topUp);
        publish(new BankToWalletTopUpBankTransferCreatedEvent(topUp.getId(), bankTransferId), "top-up-bank-transfer-created");
    }

    public void walletTransferCreated(BankToWalletTopUp topUp, WalletTransfer.WalletTransferId walletTransferId) {
        topUp.walletTransferCreated(walletTransferId);
        topUpRepository.save(topUp);
        publish(new BankToWalletTopUpWalletTransferCreatedEvent(topUp.getId(), walletTransferId), "top-up-bank-transfer-created");
    }

    public void markAsInProgress(
            BankToWalletTopUp bankToWalletTopUp) {
        bankToWalletTopUp.markAsInProgress();
        topUpRepository.save(bankToWalletTopUp);
        publish(new BankToWalletTopUpMarkedAsInProgressEvent(bankToWalletTopUp.getId()), "top-up-marked-as-in-progress");
    }

    public void bankTransferExecuted(BankToWalletTopUp bankToWalletTopUp, BankTransfer.BankTransferId bankTransferId) {

        bankToWalletTopUp.bankTransferExecuted(bankTransferId);
        BankTransfer bankTransferUpdated = bankToWalletTopUp.getBankTransfer()
                                                            .orElseThrow();
        topUpRepository.save(bankToWalletTopUp);
        publish(new BankToWalletTopUpBankTransferExecutedEvent(bankToWalletTopUp.getId(), bankTransferUpdated), "top-up-bank-transfer-executed");
    }

    private void publish(Object event, String eventName) {
        messageBroker.sendMessage(Message.of(eventName, serializationService.serialize(event)));
    }

    public void walletTransferExecuted(BankToWalletTopUp bankToWalletTopUp, WalletTransfer.WalletTransferId walletTransferId) {
        bankToWalletTopUp.walletTransferExecuted(walletTransferId);
    }

}
