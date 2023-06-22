import {Customer} from "./customer_model";

export interface CustomerRepository {
    loadAllCustomers(): Promise<Customer[]>;
    loadCustomerById(id: string): Promise<Customer | null>;
    saveCustomer(customer: Customer): Promise<Customer>;
    updateCustomer(id: string, customer: Customer): Promise<Customer | null>;
    deleteCustomer(id: string): Promise<void>;
}