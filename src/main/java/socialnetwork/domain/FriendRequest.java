package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class FriendRequest extends Entity<Long> {

    //there are IDs of Users
    private final Long fromUser;
    private final Long toUser;
    private String status;
    private static Long NUMBER_OF_FRIEND_REQUESTS = 1L;
    private LocalDateTime date = LocalDateTime.now();

    public FriendRequest(Long fromUser, Long toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.status = "pending";
        super.setId((NUMBER_OF_FRIEND_REQUESTS++));
    }

    public FriendRequest(){
        fromUser=null;
        toUser=null;
    }

    public Long getFromUser() {
        return fromUser;
    }

    public Long getToUser() {
        return toUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static void setNumberOfFriendRequests(Long number){
        NUMBER_OF_FRIEND_REQUESTS=number;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
