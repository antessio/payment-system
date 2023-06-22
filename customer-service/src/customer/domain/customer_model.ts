export interface Customer {
    id: string;
    name: string;
    email: string;
    iban: string;
}

export abstract class CustomerDomainEvent {
    id: string;

    constructor(id: string) {
        this.id = id;
    }
}

export class CustomerCreatedEvent extends CustomerDomainEvent {
    name: string;
    email: string;
    iban: string;

    constructor(id: string, name: string, email: string, iban: string) {
        super(id);
        this.name = name;
        this.email = email;
        this.iban = iban;
    }
}

export class CustomerNameUpdated extends CustomerDomainEvent {
    name: string;

    constructor(id: string, name: string) {
        super(id);
        this.name = name;
    }
}

export class CustomerEmailUpdated extends CustomerDomainEvent {
    email: string;

    constructor(id: string, email: string) {
        super(id);
        this.email = email;
    }
}

export class CustomerIbanUpdated extends CustomerDomainEvent {
    iban: string;

    constructor(id: string, iban: string) {
        super(id);
        this.iban = iban;
    }
}

export class CustomerDeleted extends CustomerDomainEvent{

}