import {Customer, CustomerDomainEvent} from "../src/customer/domain/customer_model";
import {CustomerRepository} from "../src/customer/domain/customer_repository";
import {CustomerMessageBroker} from "../src/customer/domain/customer_message_broker";


export class InMemoryCustomerRepository implements CustomerRepository {
    private customers: Customer[] = [];

    async loadAllCustomers(): Promise<Customer[]> {
        return this.customers;
    }

    async loadCustomerById(id: string): Promise<Customer | null> {
        const customer = this.customers.find((c) => c.id === id);
        return customer || null;
    }

    async saveCustomer(customer: Customer): Promise<Customer> {
        this.customers.push(customer);
        return customer;
    }

    async updateCustomer(id: string, customer: Customer): Promise<Customer | null> {
        const index = this.customers.findIndex((c) => c.id === id);

        if (index === -1) {
            return null;
        }

        this.customers[index] = customer;
        return customer;
    }

    async deleteCustomer(id: string): Promise<void> {
        this.customers = this.customers.filter((c) => c.id !== id);
    }

    loadByEmail(email: string): Promise<Customer | null> {
        return Promise.resolve(this.customers.find(c => c.email == email) || null);
    }
}




export class InMemoryMessageBroker implements CustomerMessageBroker{
    private _events: CustomerDomainEvent[] = [];

    publishEvent(event: CustomerDomainEvent) {
        this._events.push(event)
    }


    get events(): CustomerDomainEvent[] {
        return this._events;
    }
}