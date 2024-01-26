import {
    Customer,
    CustomerCreateCommand,
    CustomerCreatedEvent,
    CustomerDeleted,
    CustomerDomainEvent,
    CustomerEmailUpdated,
    CustomerIbanUpdated,
    CustomerNameUpdated,
    CustomerUpdateCommand,
    CustomerUpdateEmailCommand,
    CustomerUpdateIbanCommand,
    CustomerUpdateNameCommand
} from "../domain/customer_model";
import {CustomerRepository} from "../domain/customer_repository";
import {CustomerMessageBroker} from "../domain/customer_message_broker";
import {CustomerAlreadyExistError, CustomerNotExistingError} from "../domain/customer_error";


export interface CustomerServiceInterface {
    getAllCustomers(): Promise<Customer[]>;

    getCustomerById(id: string): Promise<Customer | null>;

    createCustomer(createCommand: CustomerCreateCommand): Promise<Customer>;

    updateCustomer(id: string, updateCommand: CustomerUpdateCommand): Promise<Customer|null>;
    updateCustomerName(id: string, updateCommand: CustomerUpdateNameCommand): Promise<Customer|null>;
    updateCustomerEmail(id: string, updateCommand: CustomerUpdateEmailCommand): Promise<Customer|null>;
    updateCustomerIban(id: string, updateCommand: CustomerUpdateIbanCommand): Promise<Customer|null>;

    deleteCustomer(id: string): Promise<void>;

    getCustomerByEmail(email: string): Promise<Customer | null>;
}

export class CustomerService implements CustomerServiceInterface {
    private repository: CustomerRepository;
    private messageBroker: CustomerMessageBroker;

    constructor(repository: CustomerRepository, messageBroker: CustomerMessageBroker) {
        this.repository = repository
        this.messageBroker = messageBroker;
    }

    createCustomer(customerCreateCommand: CustomerCreateCommand): Promise<Customer> {
        return this.repository.loadByEmail(customerCreateCommand.email).then(c=>{
            if(c){
                throw new CustomerAlreadyExistError(`customer already exist with email ${customerCreateCommand.email}`)
            }else{

                return this.repository.saveCustomer(new Customer(
                    this.repository.generateId(),
                    customerCreateCommand.name,
                    customerCreateCommand.email,
                    customerCreateCommand.iban))
                    .then(c => {

                        this.messageBroker.publishEvent(new CustomerCreatedEvent(
                            c.id,
                            c.name,
                            c.email,
                            c.iban
                        ))
                        return Promise.resolve(c);
                    });
            }
        })
    }

    deleteCustomer(id: string): Promise<void> {
        return this.repository.deleteCustomer(id)
            .then(() => {
                this.messageBroker.publishEvent(new CustomerDeleted(id))
            })
    }

    getAllCustomers(): Promise<Customer[]> {
        return this.repository.loadAllCustomers();
    }

    getCustomerById(id: string): Promise<Customer | null> {
        return this.repository.loadCustomerById(id);
    }

    updateCustomer(id: string, updateCommand: CustomerUpdateCommand): Promise<Customer|null> {
        return this.repository.loadCustomerById(id)
            .then((existingCustomer) => {
                if (!existingCustomer) {
                    throw new CustomerNotExistingError(`Customer with ID ${id} doesn't exist`);
                }

                const events: CustomerDomainEvent[] = [];

                // Check which fields have changed and add the corresponding domain events
                if (updateCommand.name && existingCustomer.name !== updateCommand.name) {
                    existingCustomer.updateName(updateCommand.name)
                    events.push(new CustomerNameUpdated(id, updateCommand.name));
                }

                if (updateCommand.email && existingCustomer.email !== updateCommand.email) {
                    existingCustomer.updateEmail(updateCommand.email)
                    events.push(new CustomerEmailUpdated(id, updateCommand.email));
                }

                if (updateCommand.iban && existingCustomer.iban !== updateCommand.iban) {
                    existingCustomer.updateIban(updateCommand.iban)
                    events.push(new CustomerIbanUpdated(id, updateCommand.iban));
                }

                // Perform the updateCommand update in the repository
                return this.repository.updateCustomer(id, existingCustomer)
                    .then((updatedCustomer) => {
                        // Publish the domain events
                        events.forEach((event) => {
                            // Assuming you have a method to publish the event
                            this.messageBroker.publishEvent(event);
                        });

                        return Promise.resolve(updatedCustomer);
                    });
            });

    }

    updateCustomerEmail(id: string, updateCommand: CustomerUpdateEmailCommand): Promise<Customer | null> {
        return this.repository.loadCustomerById(id)
            .then((existingCustomer) => {
                if (!existingCustomer) {
                    throw new Error(`Customer with ID ${id} doesn't exist`);
                }




                const events: CustomerDomainEvent[] = [];


                if (updateCommand.email && existingCustomer.email !== updateCommand.email) {
                    existingCustomer.updateEmail(updateCommand.email)
                    events.push(new CustomerEmailUpdated(id, updateCommand.email));
                }

                // Perform the updateCommand update in the repository
                return this.repository.updateCustomer(id, existingCustomer)
                    .then((updatedCustomer) => {
                        // Publish the domain events
                        events.forEach((event) => {
                            // Assuming you have a method to publish the event
                            this.messageBroker.publishEvent(event);
                        });

                        return Promise.resolve(updatedCustomer);
                    });
            });
    }

    updateCustomerIban(id: string, updateCommand: CustomerUpdateIbanCommand): Promise<Customer | null> {
        return this.repository.loadCustomerById(id)
            .then((existingCustomer) => {
                if (!existingCustomer) {
                    throw new Error(`Customer with ID ${id} doesn't exist`);
                }




                const events: CustomerDomainEvent[] = [];

                if (updateCommand.iban && existingCustomer.iban !== updateCommand.iban) {
                    existingCustomer.updateIban(updateCommand.iban)
                    events.push(new CustomerIbanUpdated(id, updateCommand.iban));
                }

                // Perform the updateCommand update in the repository
                return this.repository.updateCustomer(id, existingCustomer)
                    .then((updatedCustomer) => {
                        // Publish the domain events
                        events.forEach((event) => {
                            // Assuming you have a method to publish the event
                            this.messageBroker.publishEvent(event);
                        });

                        return Promise.resolve(updatedCustomer);
                    });
            });
    }
    updateCustomerName(id: string, updateCommand: CustomerUpdateNameCommand): Promise<Customer | null> {
        return this.repository.loadCustomerById(id)
            .then((existingCustomer) => {
                if (!existingCustomer) {
                    throw new Error(`Customer with ID ${id} doesn't exist`);
                }



                const events: CustomerDomainEvent[] = [];

                // Check which fields have changed and add the corresponding domain events
                if (updateCommand.name && existingCustomer.name !== updateCommand.name) {
                    existingCustomer.updateName(updateCommand.name)
                    events.push(new CustomerNameUpdated(id, updateCommand.name));
                }
                // Perform the updateCommand update in the repository
                return this.repository.updateCustomer(id, existingCustomer)
                    .then((updatedCustomer) => {
                        // Publish the domain events
                        events.forEach((event) => {
                            // Assuming you have a method to publish the event
                            this.messageBroker.publishEvent(event);
                        });

                        return Promise.resolve(updatedCustomer);
                    });
            });
    }

    getCustomerByEmail(email: string): Promise<Customer | null> {
        return this.repository.loadByEmail(email);
    }


}