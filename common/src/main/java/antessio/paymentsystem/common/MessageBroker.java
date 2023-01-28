package antessio.paymentsystem.common;

import java.util.List;

public interface MessageBroker {
    void sendMessage(Message message);

    void sendMessages(List<Message> messages);
    void subscribe(Subscriber subscriber);


}
