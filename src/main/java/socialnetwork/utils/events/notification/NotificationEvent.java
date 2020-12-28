package socialnetwork.utils.events.notification;

import socialnetwork.domain.Notification;
import socialnetwork.utils.events.Event;

public class NotificationEvent implements Event {
    private final NotificationEventType type;
    private final Notification data;
    private Notification oldData;

    public NotificationEvent(NotificationEventType type, Notification data) {
        this.type = type;
        this.data = data;
    }

    public NotificationEvent(NotificationEventType type, Notification data, Notification oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public NotificationEventType getType() {
        return type;
    }

    public Notification getData() {
        return data;
    }

    public Notification getOldData() {
        return oldData;
    }
}
