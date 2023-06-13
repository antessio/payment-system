package antessio.paymentsystem.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperSerializationService implements SerializationService{

    private final ObjectMapper objectMapper;

    public ObjectMapperSerializationService() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public <T> String serialize(T obj) {
        try {
            return objectMapper.writeValueAsString(objectMapper);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(String raw, Class<T> cls) {
        try {
            return objectMapper.readValue(raw, cls);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
