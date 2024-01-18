package antessio.paymentsystem.topup.domain;

import org.jmolecules.ddd.annotation.ValueObject;

import antessio.paymentsystem.bank.BankTransferId;
import antessio.paymentsystem.bank.BankTransferStatus;


@ValueObject
public record BankTransfer(BankTransferId id,
                           BankTransferStatus status) {


}
