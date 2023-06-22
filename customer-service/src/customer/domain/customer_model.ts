export interface CustomerUpdateCommand{
    name?: string;
    email?: string;
    iban?: string;
}

export interface CustomerUpdateNameCommand{
    name: string;
}

export interface CustomerUpdateEmailCommand{
    email: string;
}
export interface CustomerUpdateIbanCommand{
    iban: string;
}

export interface CustomerCreateCommand{
    name: string;
    email: string;
    iban: string;
}

export class Customer {
    private readonly _id: string;
    private _name: string;
    private _email: string;
    private _iban: string;


    constructor(id: string, name: string, email: string, iban: string) {
        this._id = id;
        this._name = name;
        this._email = email;
        this._iban = iban;
    }


    get id(): string {
        return this._id;
    }

    get name(): string {
        return this._name;
    }

    get email(): string {
        return this._email;
    }

    get iban(): string {
        return this._iban;
    }


    updateName(value: string): void{
        this._name = value;
    }

    updateEmail(value: string): void{
        this._email = value;
    }

    updateIban(value: string):void {
        this._iban = value;
    }
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