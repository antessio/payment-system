import request from 'supertest';
import express from 'express';
import bodyParser from 'body-parser';
import customerRoutes from '../routes';
import { InMemoryCustomerRepository } from '../repository';
import { Customer } from '../models';

const app = express();
const repository = new InMemoryCustomerRepository();
app.use(bodyParser.json());
app.use('/api', customerRoutes(repository));

const testCustomer: Customer = {
    id: '1',
    name: 'John Doe',
    email: 'john@example.com',
    iban: 'NL91ABNA0417164300',
};

beforeEach(async () => {
    // Clear the repository before each test
    for (const c of (await repository.getAllCustomers())) {
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
        await repository.createCustomer(testCustomer);

        const response = await request(app).get('/api/customers');
        expect(response.status).toBe(200);
        expect(response.body).toEqual([testCustomer]);
    });
});

describe('GET /api/customers/:id', () => {
    it('should return 404 when customer does not exist', async () => {
        const response = await request(app).get('/api/customers/1');
        expect(response.status).toBe(404);
        expect(response.body).toEqual({ message: 'Customer not found' });
    });

    it('should return the customer when it exists', async () => {
        await repository.createCustomer(testCustomer);

        const response = await request(app).get(`/api/customers/${testCustomer.id}`);
        expect(response.status).toBe(200);
        expect(response.body).toEqual(testCustomer);
    });
});

describe('POST /api/customers', () => {
    it('should create a new customer', async () => {
        const response = await request(app)
            .post('/api/customers')
            .send(testCustomer);

        expect(response.status).toBe(201);
        expect(response.body).toEqual(testCustomer);
    });

    it('should return 400 when request body is invalid', async () => {
        const response = await request(app)
            .post('/api/customers')
            .send({});

        expect(response.status).toBe(400);
        expect(response.body).toEqual({ message: 'Invalid input' });
    });

    it('should return 409 when customer ID already exists', async () => {
        await repository.createCustomer(testCustomer);

        const response = await request(app)
            .post('/api/customers')
            .send(testCustomer);

        expect(response.status).toBe(409);
        expect(response.body).toEqual({ message: 'Customer ID already exists' });
    });
});

describe('PUT /api/customers/:id', () => {
    it('should update an existing customer', async () => {
        await repository.createCustomer(testCustomer);

        const updatedCustomer: Customer = {
            id: '1',
            name: 'Jane Doe',
            email: 'jane@example.com',
            iban: 'NL91ABNA0417164300',
        };

        const response = await request(app)
            .put(`/api/customers/${testCustomer.id}`)
            .send(updatedCustomer);

        expect(response.status).toBe(200);
        expect(response.body).toEqual(updatedCustomer);
    });

    it('should return 404 when customer does not exist', async () => {
        const response = await request(app)
            .put('/api/customers/1')
            .send(testCustomer);

        expect(response.status).toBe(404);
        expect(response.body).toEqual({ message: 'Customer not found' });
    });
});

describe('DELETE /api/customers/:id', () => {
    it('should delete an existing customer', async () => {
        await repository.createCustomer(testCustomer);

        const response = await request(app).delete(`/api/customers/${testCustomer.id}`);

        expect(response.status).toBe(200);
        expect(response.body).toEqual({ message: 'Customer deleted' });
    });

    it('should return 404 when customer does not exist', async () => {
        const response = await request(app).delete('/api/customers/1');

        expect(response.status).toBe(404);
        expect(response.body).toEqual({ message: 'Customer not found' });
    });
});