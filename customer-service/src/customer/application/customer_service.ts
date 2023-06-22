import {
    Customer,
    CustomerCreatedEvent,
    CustomerDeleted,
    CustomerDomainEvent,
    CustomerEmailUpdated,
    CustomerIbanUpdated,
    CustomerNameUpdated
} from "../domain/customer_model";
import {CustomerRepository} from "../domain/customer_repository";
import {CustomerMessageBroker} from "../domain/customer_message_broker";

export interface CustomerServiceInterface {
    getAllCustomers(): Promise<Customer[]>;

    getCustomerById(id: string): Promise<Customer | null>;

    createCustomer(customer: Customer): Promise<Customer>;

    updateCustomer(id: string, customer: Customer): Promise<Customer | null>;

    deleteCustomer(id: string): Promise<void>;
}

export class CustomerService implements CustomerServiceInterface {
    private repository: CustomerRepository;
    private messageBroker: CustomerMessageBroker;

    constructor(repository: CustomerRepository, messageBroker: CustomerMessageBroker) {
        this.repository = repository
        this.messageBroker = messageBroker;
    }

    createCustomer(customer: Customer): Promise<Customer> {
        return this.repository.saveCustomer(customer)
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

    updateCustomer(id: string, customer: Customer): Promise<Customer | null> {
        return this.repository.loadCustomerById(id)
            .then((existingCustomer) => {
                if (!existingCustomer) {
                    throw new Error(`Customer with ID ${id} doesn't exist`);
                }

                const updatedCustomer = {...existingCustomer, ...customer};

                const events: CustomerDomainEvent[] = [];

                // Check which fields have changed and add the corresponding domain events
                if (existingCustomer.name !== updatedCustomer.name) {
                    events.push(new CustomerNameUpdated(id, updatedCustomer.name));
                }

                if (existingCustomer.email !== updatedCustomer.email) {
                    events.push(new CustomerEmailUpdated(id, updatedCustomer.email));
                }

                if (existingCustomer.iban !== updatedCustomer.iban) {
                    events.push(new CustomerIbanUpdated(id, updatedCustomer.iban));
                }

                // Perform the customer update in the repository
                return this.repository.updateCustomer(id, updatedCustomer)
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


}