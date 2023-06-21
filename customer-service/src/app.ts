import express from 'express';
import bodyParser from 'body-parser';
import customerRoutes from './routes';

const app = express();
const port = 3000;

// Middleware
app.use(bodyParser.json());

// Routes
app.use('/api', customerRoutes);

// Start the server
app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});
