package socialnetwork.utils.events.event;

import socialnetwork.domain.Event;

public class EventEvent implements socialnetwork.utils.events.Event {
    private final EventEventType type;
    private final Event data;
    private Event oldData;

    public EventEvent(EventEventType type, Event data) {
        this.type = type;
        this.data = data;
    }

    public EventEvent(EventEventType type, Event data, Event oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public EventEventType getType() {
        return type;
    }

    public Event getData() {
        return data;
    }

    public Event getOldData() {
        return oldData;
    }
}
