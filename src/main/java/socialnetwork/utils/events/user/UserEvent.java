package socialnetwork.utils.events.user;

import socialnetwork.domain.User;
import socialnetwork.utils.events.Event;

public class UserEvent implements Event {
    private final UserEventType type;
    private final User data;
    private User oldData;

    public UserEvent(UserEventType type, User data) {
        this.type = type;
        this.data = data;
    }

    public UserEvent(UserEventType type, User data, User oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public UserEventType getType() {
        return type;
    }

    public User getData() {
        return data;
    }

    public User getOldData() {
        return oldData;
    }
}
