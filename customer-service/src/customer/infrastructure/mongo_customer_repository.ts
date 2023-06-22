// mongo_customer_repository.ts
import { Customer } from '../domain/customer_model';
import { MongoClient, Collection } from 'mongodb';
import {CustomerRepository} from "../domain/customer_repository";
import e from "express";



export class MongoCustomerRepository implements CustomerRepository {
    private collection: Collection<Customer>;

    constructor() {
        const url = process.env.MONGODB_URL || 'mongodb://localhost:27017';
        const dbName = process.env.MONGODB_DB_NAME || 'payment_system';
        const collectionName = process.env.MONGODB_COLLECTION_NAME || 'customers';
        const client = new MongoClient(url)
        const db = client.db(dbName);
        this.collection = db.collection(collectionName);
    }

    async loadAllCustomers(): Promise<Customer[]> {
        return this.collection.find().toArray();
    }

    async loadCustomerById(id: string): Promise<Customer | null> {
        return this.collection.findOne({ id });
    }

    async saveCustomer(customer: Customer): Promise<Customer> {
        await this.collection.insertOne(customer);
        return customer;
    }

    async updateCustomer(id: string, customer: Customer): Promise<Customer | null> {
        const result = await this.collection.findOneAndUpdate(
            { id },
            { $set: customer },
            { returnDocument: 'after' }
        );

        return result.value || null;
    }

    async deleteCustomer(id: string): Promise<void> {
        await this.collection.deleteOne({ id });
    }

    loadByEmail(email: string): Promise<Customer | null> {
        return this.collection.findOne({email: email})
    }
}
