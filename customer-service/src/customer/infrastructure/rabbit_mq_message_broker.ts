import { Connection, Channel, connect } from 'amqplib';
import {CustomerMessageBroker} from "../domain/customer_message_broker";
import {CustomerDomainEvent} from "../domain/customer_model";

export class RabbitMQCustomerMessageBroker implements CustomerMessageBroker {
    private connection: Connection | null = null;
    private channel: Channel | null = null;
    private readonly rabbitMQUrl: string

    constructor() {
        this.rabbitMQUrl = process.env.RABBIT_MQ_URL || 'amqp://myuser:mypassword@localhost:5672';
    }

    async publishEvent(event: CustomerDomainEvent) {
        const channel = await this.getChannel();
        const exchangeName = 'customer_events';

        await channel.assertExchange(exchangeName, 'fanout', { durable: false });

        const message = Buffer.from(JSON.stringify(event));
        channel.publish(exchangeName, '', message);
    }

    private async getChannel(): Promise<Channel> {
        if (this.channel) {
            return this.channel;
        }

        const connection = await this.getConnection();
        this.channel = await connection.createChannel();

        return this.channel;
    }

    private async getConnection(): Promise<Connection> {
        if (this.connection) {
            return this.connection;
        }

        this.connection = await connect(this.rabbitMQUrl);

        return this.connection;
    }
}
