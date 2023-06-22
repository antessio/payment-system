
export class CustomerError extends Error{
    constructor(message:string) {
        super(message);
    }
}

export class CustomerAlreadyExistError extends CustomerError{}

export class CustomerNotExistingError extends CustomerError{}