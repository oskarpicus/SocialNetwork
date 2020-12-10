package socialnetwork.utils.events.friendRequest;

import socialnetwork.domain.FriendRequest;
import socialnetwork.utils.events.Event;

public class FriendRequestEvent implements Event {
    private final FriendRequestEventType type;
    private final FriendRequest data;
    private FriendRequest oldData;

    public FriendRequestEvent(FriendRequestEventType type, FriendRequest data) {
        this.type = type;
        this.data = data;
    }

    public FriendRequestEvent(FriendRequestEventType type, FriendRequest data, FriendRequest oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public FriendRequestEventType getType() {
        return type;
    }

    public FriendRequest getData() {
        return data;
    }

    public FriendRequest getOldData() {
        return oldData;
    }
}
