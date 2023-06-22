import request from 'supertest';
import express from 'express';
import bodyParser from 'body-parser';
import customerRoutes from '../src/customer/infrastructure/customer_routes';
import {Customer, CustomerCreateCommand} from '../src/customer/domain/customer_model';
import {InMemoryCustomerRepository, InMemoryMessageBroker} from "./testHelpers";
import {CustomerService} from "../src/customer/application/customer_service";

const app = express();
const repository = new InMemoryCustomerRepository();
app.use(bodyParser.json());
let inMemoryMessageBroker = new InMemoryMessageBroker();
app.use('/api', customerRoutes(new CustomerService(repository, inMemoryMessageBroker)));

function getTestCustomer(id: string, name: string, email: string, iban: string): Customer {
    return new Customer(id, name, email, iban)
}

function toCustomerCreateCommand(customer: Customer): CustomerCreateCommand {
    return {
        name: customer.name,
        email: customer.email,
        iban: customer.iban
    }
}

function toObject(customer: Customer): object{
    return {
        id: customer.id,
        name: customer.name,
        email: customer.email,
        iban: customer.iban
    }
}

const testCustomer: Customer = getTestCustomer('1', 'John Doe', 'john@example.com', 'NL91ABNA0417164300');

beforeEach(async () => {
    // Clear the repository before each test
    for (const c of (await repository.loadAllCustomers())) {
        await repository.deleteCustomer(c.id);
    }
});

describe('GET /api/customers', () => {
    it('should return an empty array when no customers exist', async () => {
        const response = await request(app).get('/api/customers');
        expect(response.status).toBe(200);
        expect(response.body).toEqual([]);
    });

    it('should return an array of customers', async () => {
        await repository.saveCustomer(testCustomer);

        const response = await request(app).get('/api/customers');
        expect(response.status).toBe(200);
        expect(response.body).toEqual([toObject(testCustomer)]);
    });
});

describe('GET /api/customers/:id', () => {
    it('should return 404 when customer does not exist', async () => {
        const response = await request(app).get('/api/customers/1');
        expect(response.status).toBe(404);
        expect(response.body).toEqual({message: 'Customer not found'});
    });

    it('should return the customer when it exists', async () => {
        await repository.saveCustomer(testCustomer);

        const response = await request(app).get(`/api/customers/${testCustomer.id}`);
        expect(response.status).toBe(200);
        expect(response.body).toEqual(toObject(testCustomer));
    });
});

describe('POST /api/customers', () => {
    let createCommand = toCustomerCreateCommand(testCustomer);
    it('should create a new customer', async () => {
        const response = await request(app)
            .post('/api/customers')
            .send(createCommand);

        expect(response.status).toBe(201);
        expect(response.body).toMatchObject(expect.objectContaining({
            id: expect.anything(),
            ...createCommand,

        }))
    });

    it('should return 400 when request body is invalid', async () => {
        const response = await request(app)
            .post('/api/customers')
            .send({});

        expect(response.status).toBe(400);
        expect(response.body).toEqual({message: 'Invalid input'});
    });

    it('should return 409 when customer ID already exists', async () => {
        await repository.saveCustomer(testCustomer);

        const response = await request(app)
            .post('/api/customers')
            .send(createCommand);

        expect(response.status).toBe(409);
        expect(response.body).toEqual({message: 'Customer ID already exists'});
    });
});

describe('PUT /api/customers/:id', () => {
    it('should update an existing customer', async () => {
        await repository.saveCustomer(testCustomer);



        let updateRequest = {
            name: "Jane Doe",
            email: "jane.doe@email.com",
            iban: "IT121412412"
        };
        const response = await request(app)
            .put(`/api/customers/${testCustomer.id}`)
            .send(updateRequest);

        expect(response.status).toBe(200);
        expect(response.body).toEqual({
            ...updateRequest,
            id: testCustomer.id
        });
    });

    it('should return 404 when customer does not exist', async () => {
        const response = await request(app)
            .put('/api/customers/1')
            .send(testCustomer);

        expect(response.status).toBe(404);
        expect(response.body).toEqual({message: 'Customer not found'});
    });
});

describe('DELETE /api/customers/:id', () => {
    it('should delete an existing customer', async () => {
        await repository.saveCustomer(testCustomer);

        const response = await request(app).delete(`/api/customers/${testCustomer.id}`);

        expect(response.status).toBe(200);
        expect(response.body).toEqual({message: 'Customer deleted'});
    });

    it('should return 404 when customer does not exist', async () => {
        const response = await request(app).delete('/api/customers/1');

        expect(response.status).toBe(404);
        expect(response.body).toEqual({message: 'Customer not found'});
    });
});
