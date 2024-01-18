package antessio.paymentsystem.topup;

import java.util.Optional;

import antessio.paymentsystem.topup.dto.BankToWalletTopUpCreateDto;
import antessio.paymentsystem.topup.dto.TopUpDto;

public interface TopUpService {

    Optional<TopUpDto> loadById(TopUpId id);

    TopUpId createBankToWalletTopUp(BankToWalletTopUpCreateDto createDto);

}
