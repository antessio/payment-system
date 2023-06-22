import express from 'express';
import bodyParser from 'body-parser';
import customerRoutes from './customer/infrastructure/customer_routes';
import {MongoCustomerRepository} from "./customer/infrastructure/mongo_customer_repository";
import {CustomerService} from "./customer/application/customer_service";
import {InMemoryMessageBroker} from "./customer/infrastructure/in_memory_message_broker";

const app = express();
const port = 3000;

// Middleware
app.use(bodyParser.json());

// Routes
app.use('/api', customerRoutes(new CustomerService(new MongoCustomerRepository(), new InMemoryMessageBroker())));

// Start the server
app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});
