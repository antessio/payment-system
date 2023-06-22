import {CustomerMessageBroker} from "../domain/customer_message_broker";
import {CustomerDomainEvent} from "../domain/customer_model";


export class InMemoryMessageBroker implements CustomerMessageBroker{
    private _events: CustomerDomainEvent[] = [];

    publishEvent(event: CustomerDomainEvent) {
        this._events.push(event)
    }


    get events(): CustomerDomainEvent[] {
        return this._events;
    }
}