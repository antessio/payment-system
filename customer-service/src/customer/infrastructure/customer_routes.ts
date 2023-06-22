// customer_routes.ts
import express, {Request, Response} from 'express';
import {Customer, CustomerCreateCommand, CustomerUpdateCommand} from '../domain/customer_model';
import {CustomerServiceInterface} from "../application/customer_service";
import {CustomerAlreadyExistError, CustomerNotExistingError} from "../domain/customer_error";

const router = express.Router();

interface CustomerDto {
    id: string,
    name: string,
    iban: string,
    email: string
}

function convertToDTO(customer: Customer): CustomerDto {
    return {
        id: customer.id,
        name: customer.name,
        email: customer.email,
        iban: customer.iban
    }
}

function customerRoutes(customerService: CustomerServiceInterface) {
    router.get('/customers', async (req: Request, res: Response) => {
        const customers = (await customerService.getAllCustomers())
            .map(c => convertToDTO(c));
        res.json(customers);

    });

    router.get('/customers/:id', async (req: Request, res: Response) => {
        const customerId = req.params.id;
        const customer = await customerService.getCustomerById(customerId);
        if (!customer) {
            return res.status(404).json({message: 'Customer not found'});
        }
        res.json(convertToDTO(customer));
    });

    router.post('/customers', async (req: Request, res: Response) => {
        const newCustomer: CustomerCreateCommand = req.body;

        // Validate input
        if (!newCustomer.name || !newCustomer.email || !newCustomer.iban) {
            return res.status(400).json({message: 'Invalid input'});
        }

        //TODO: Implement idempotency

        try {
            // Add the new customer to the array
            const customerCreated = await customerService.createCustomer(newCustomer);

            res.status(201).json(convertToDTO(customerCreated));

        } catch (error) {
            if (error instanceof CustomerAlreadyExistError) {
                res.status(409).json({message: 'Customer ID already exists'})
            } else {
                res.status(500).json({message: 'Internal server error'});
            }
        }
    });

    router.put('/customers/:id', async (req: Request, res: Response) => {
        const customerId = req.params.id;
        const updatedCustomerRequest: CustomerUpdateCommand = req.body;


        // Update the customer
        try {
            const updatedCustomer = await customerService.updateCustomer(customerId, updatedCustomerRequest);
            if (updatedCustomer){
                res.status(200).json(convertToDTO(updatedCustomer))
            }else{
                res.status(404)
            }
        } catch (error) {
            if (error instanceof CustomerNotExistingError) {
                res.status(404).json({message: 'Customer not found'});
            } else {
                res.status(500).json({message: 'Internal server error'});
            }
        }

    });

    router.delete('/customers/:id', async (req: Request, res: Response) => {
        const customerId = req.params.id;

        // Find the customer to delete
        const customer = await customerService.getCustomerById(customerId)
        if (!customer) {
            return res.status(404).json({message: 'Customer not found'});
        }
        // Remove the customer from the array
        await customerService.deleteCustomer(customerId)


        res.json({message: 'Customer deleted'});
    });
    return router;

}


export default customerRoutes;
