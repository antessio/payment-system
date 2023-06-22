import {CustomerDomainEvent} from "./customer_model";

export interface CustomerMessageBroker {

    publishEvent(event: CustomerDomainEvent)
}