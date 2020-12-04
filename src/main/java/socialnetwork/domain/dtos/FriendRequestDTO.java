package socialnetwork.domain.dtos;

import socialnetwork.domain.User;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;

public class FriendRequestDTO {

    private final Long id; //id of the friend request
    private final Long userFromId;
    private final Long userToId;
    private final String fromFirstName;
    private final String fromLastName;
    private final String toFirstName;
    private final String toLastName;
    private final String status;
    private final String dateAsString;

    public FriendRequestDTO(Long id, User fromUser, User toUser, String status, LocalDateTime date) {
        this.id=id;
        this.userFromId=fromUser.getId();
        this.fromFirstName=fromUser.getFirstName();
        this.fromLastName=fromUser.getLastName();
        this.userToId=toUser.getId();
        this.toFirstName=toUser.getFirstName();
        this.toLastName=toUser.getLastName();
        this.status = status;
        dateAsString= Constants.DATE_TIME_FORMATTER.format(date);
    }

    public String getFromFirstName() {
        return fromFirstName;
    }

    public String getFromLastName() {
        return fromLastName;
    }

    public String getStatus() {
        return status;
    }

    public String getDateAsString() {
        return dateAsString;
    }

    public Long getId() {
        return id;
    }

    public Long getUserFromId() {
        return userFromId;
    }

    public Long getUserToId() {
        return userToId;
    }

    public String getToFirstName() {
        return toFirstName;
    }

    public String getToLastName() {
        return toLastName;
    }
}
