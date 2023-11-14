package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

import antessio.paymentsystem.topup.TopUpId;

@DomainEvent
public record TopUpMarkedAsInProgressEvent(TopUpId id) {

}
