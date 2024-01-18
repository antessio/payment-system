package antessio.paymentsystem.bank.bankapp.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class HttpBadRequestException extends RuntimeException{

}
