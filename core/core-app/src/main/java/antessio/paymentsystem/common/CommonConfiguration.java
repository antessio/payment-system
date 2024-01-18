package antessio.paymentsystem.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

    @Bean
    public MessageBroker messageBroker() {
        return new SimpleMessageBroker();
    }

    @Bean
    public SerializationService serializationService() {
        return new ObjectMapperSerializationService();
    }

}
