package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record TopUpMarkedAsInProgressEvent(TopUp.TopUpId id) {

}
