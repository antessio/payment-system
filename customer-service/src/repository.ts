// repository.ts
import { Customer } from './models';

export interface CustomerRepository {
    getAllCustomers(): Promise<Customer[]>;
    getCustomerById(id: string): Promise<Customer | null>;
    createCustomer(customer: Customer): Promise<Customer>;
    updateCustomer(id: string, customer: Customer): Promise<Customer | null>;
    deleteCustomer(id: string): Promise<void>;
}

export class InMemoryCustomerRepository implements CustomerRepository {
    private customers: Customer[] = [];

    async getAllCustomers(): Promise<Customer[]> {
        return this.customers;
    }

    async getCustomerById(id: string): Promise<Customer | null> {
        const customer = this.customers.find((c) => c.id === id);
        return customer || null;
    }

    async createCustomer(customer: Customer): Promise<Customer> {
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
}
