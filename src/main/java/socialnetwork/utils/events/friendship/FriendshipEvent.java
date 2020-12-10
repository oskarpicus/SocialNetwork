package socialnetwork.utils.events.friendship;

import socialnetwork.domain.Friendship;
import socialnetwork.utils.events.Event;

public class FriendshipEvent implements Event {
    private final FriendshipEventType type;
    private final Friendship data;
    private Friendship oldData;

    public FriendshipEvent(FriendshipEventType type, Friendship data) {
        this.type = type;
        this.data = data;
    }

    public FriendshipEvent(FriendshipEventType type, Friendship data, Friendship oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public FriendshipEventType getType() {
        return type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }
}
