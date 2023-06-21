// routes.ts
import express, { Request, Response } from 'express';
import { Customer } from './models';
import {CustomerRepository} from "./repository";

const router = express.Router();



function customerRoutes(customerRepository: CustomerRepository) {
    router.get('/customers', async (req: Request, res: Response) => {
        const customers = await customerRepository.getAllCustomers();
        res.json(customers);

    });

    router.get('/customers/:id', async (req: Request, res: Response) => {
        const customerId = req.params.id;
        const customer = await customerRepository.getCustomerById(customerId);
        if (!customer) {
            return res.status(404).json({message: 'Customer not found'});
        }
        res.json(customer);
    });

    router.post('/customers', async (req: Request, res: Response) => {
        const newCustomer: Customer = req.body;

        // Validate input
        if (!newCustomer.id || !newCustomer.name || !newCustomer.email || !newCustomer.iban) {
            return res.status(400).json({message: 'Invalid input'});
        }

        // Check if a customer with the same ID already exists
        const existingCustomer = await customerRepository.getCustomerById(newCustomer.id)
        if (existingCustomer) {
            return res.status(409).json({message: 'Customer ID already exists'});
        }

        // Add the new customer to the array
        await customerRepository.createCustomer(newCustomer);

        res.status(201).json(newCustomer);
    });

    router.put('/customers/:id', async (req: Request, res: Response) => {
        const customerId = req.params.id;
        const updatedCustomerRequest: Customer = req.body;

        // Find the customer to update
        const customer = await customerRepository.getCustomerById(customerId);

        if (!customer) {
            return res.status(404).json({message: 'Customer not found'});
        }
        const updatedCustomer = {
            ...customer,
            ...updatedCustomerRequest
        }

        // Update the customer
        await customerRepository.updateCustomer(customerId, updatedCustomer);

        res.json(updatedCustomer);
    });

    router.delete('/customers/:id', async (req: Request, res: Response) => {
        const customerId = req.params.id;

        // Find the customer to delete
        const customer = await customerRepository.getCustomerById(customerId)
        if (!customer) {
            return res.status(404).json({message: 'Customer not found'});
        }
        // Remove the customer from the array
        await customerRepository.deleteCustomer(customerId)



        res.json({message: 'Customer deleted'});
    });
    return router;

}


export default customerRoutes;
