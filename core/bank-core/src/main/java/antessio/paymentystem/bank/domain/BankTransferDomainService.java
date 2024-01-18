package antessio.paymentystem.bank.domain;

import java.time.LocalDate;

import org.jmolecules.ddd.annotation.Service;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.common.Message;
import antessio.paymentsystem.common.MessageBroker;
import antessio.paymentsystem.common.SerializationService;
import antessio.paymentsystem.topup.BankAccountId;

@Service
public class BankTransferDomainService {

    private static final String BANK_TRANSFER_CREATED_EVENT_NAME = "bank-transfer-created";
    private static final String BANK_TRANSFER_EXECUTED_EVENT_NAME = "bank-transfer-executed";
    private static final String BANK_TRANSFER_CANCELED_EVENT_NAME = "bank-transfer-canceled";
    private static final String BANK_TRANSFER_FAILED_EVENT_NAME = "bank-transfer-failed";
    private static final String BANK_TRANSFER_REVERTED_EVENT_NAME = "bank-transfer-reverted";
    private final BankTransferRepository bankTransferRepository;
    private final MessageBroker messageBroker;
    private final SerializationService serializationService;

    public BankTransferDomainService(BankTransferRepository bankTransferRepository, MessageBroker messageBroker, SerializationService serializationService) {
        this.bankTransferRepository = bankTransferRepository;
        this.messageBroker = messageBroker;
        this.serializationService = serializationService;
    }

    public BankTransfer createBankTransfer(Amount amount, BankAccountId bankAccountId) {
        BankTransfer bankTransfer = new BankTransfer(amount, bankAccountId);

        bankTransferRepository.save(bankTransfer);
        publish(new BankTransferCreatedEvent(bankTransfer.getId(), bankTransfer.getAmount(), bankTransfer.getBankAccountId()), BANK_TRANSFER_CREATED_EVENT_NAME);
        return bankTransfer;
    }

    public void executeBankTransfer(BankTransfer bankTransfer, LocalDate localDate) {
        bankTransfer.execute(localDate);
        save(bankTransfer);
        publish(new BankTransferExecutedEvent(bankTransfer.getId(), bankTransfer.getExecutionDate().orElseThrow()), BANK_TRANSFER_EXECUTED_EVENT_NAME);
    }
    public void cancelBankTransfer(BankTransfer bankTransfer) {
        bankTransfer.cancel();
        save(bankTransfer);
        publish(new BankTransferCanceledEvent(bankTransfer.getId()), BANK_TRANSFER_CANCELED_EVENT_NAME);
    }

    public void failBankTransfer(BankTransfer bankTransfer) {
        bankTransfer.fail();
        save(bankTransfer);
        publish(new BankTransferFailedEvent(bankTransfer.getId()), BANK_TRANSFER_FAILED_EVENT_NAME);
    }
    public void revertBankTransfer(BankTransfer bankTransfer, LocalDate localDate) {
        bankTransfer.revert(localDate);
        save(bankTransfer);
        publish(new BankTransferRevertedEvent(bankTransfer.getId(), bankTransfer.getExecutionDate().orElseThrow()), BANK_TRANSFER_REVERTED_EVENT_NAME);
    }
    private void save(BankTransfer bankTransfer) {
        bankTransferRepository.save(bankTransfer);
    }

    private void publish(Object event, String eventName) {
        messageBroker.sendMessage(Message.of(eventName, serializationService.serialize(event)));
    }


}
