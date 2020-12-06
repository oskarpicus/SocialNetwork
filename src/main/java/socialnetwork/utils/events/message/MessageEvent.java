package socialnetwork.utils.events.message;

import socialnetwork.domain.Message;
import socialnetwork.utils.events.Event;

public class MessageEvent implements Event {
    private final MessageEventType type;
    private final Message data;
    private Message oldData;

    public MessageEvent(MessageEventType type, Message data) {
        this.type = type;
        this.data = data;
    }

    public MessageEvent(MessageEventType type, Message data, Message oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public MessageEventType getType() {
        return type;
    }

    public Message getData() {
        return data;
    }

    public Message getOldData() {
        return oldData;
    }
}
