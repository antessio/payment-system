package antessio.paymentsystem.common;

public interface SerializationService {

    <T> String serialize(T obj);

    <T> T deserialize(String raw, Class<T> cls);

}
