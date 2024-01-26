import {Customer} from '../domain/customer_model';
import {Collection, Document, MongoClient, OptionalId, WithId, ObjectId} from 'mongodb';
import dotenv from 'dotenv';

import {CustomerRepository} from "../domain/customer_repository";
import {randomUUID} from "crypto";

dotenv.config();


export class MongoCustomerRepository implements CustomerRepository {
    private collection: Collection;

    constructor() {
        const url = process.env.MONGODB_URL || 'mongodb://localhost:27017';
        const dbName = process.env.MONGODB_DB_NAME || 'payment_system';
        const collectionName = process.env.MONGODB_COLLECTION_NAME || 'customers';
        const client = new MongoClient(url)
        const db = client.db(dbName);
        this.collection = db.collection(collectionName);
    }

    generateId(): string {
        return randomUUID().toString()
    }

    async loadAllCustomers(): Promise<Customer[]> {
        return this.collection.find({})
            .map(c => this.fromMongoDb(c))
            .toArray()
    }

    private fromMongoDb(c: WithId<Document>) {
        return new Customer(c._id.toString(),
            c._name,
            c._email,
            c._iban);
    }

    async loadCustomerById(id: string): Promise<Customer | null> {

        return this.collection.findOne({id: id})
            .then(c => c ? this.fromMongoDb(c) : null);
    }

    async saveCustomer(customer: Customer): Promise<Customer> {

        return await this.collection.insertOne({...customer, id: customer.id} as OptionalId<Customer>)
            .then(r => {
                const id = r.insertedId.toString()
                return new Customer(id, customer.name, customer.email, customer.iban)
            });
    }

    async updateCustomer(id: string, customer: Customer): Promise<Customer | null> {
        const result = await this.collection.findOneAndUpdate(
            { id },
            { $set: customer },
            { returnDocument: 'after' }
        ).then(c => {

            return c.value ? new Customer(c.value._id.toString(),
                c.value._name,
                c.value._email,
                c.value._iban) : null
        });

        return result || null;
    }

    async deleteCustomer(id: string): Promise<void> {
        await this.collection.deleteOne({ id });
    }

    async loadByEmail(email: string): Promise<Customer | null> {
        return this.collection.findOne({_email: email}).then(c => {
            return c ? this.fromMongoDb(c) : null
        })
    }
}
