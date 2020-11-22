package socialnetwork.domain.dtos;

import socialnetwork.utils.Constants;

import java.time.LocalDateTime;

public class FriendRequestDTO {

    private final Long id;
    private final String fromFirstName;
    private final String fromLastName;
    private String status;
    private final LocalDateTime date;
    private final String dateAsString;


    public FriendRequestDTO(Long id,String fromFirstName, String fromLastName, String status, LocalDateTime date) {
        this.id=id;
        this.fromFirstName = fromFirstName;
        this.fromLastName = fromLastName;
        this.status = status;
        this.date = date;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }
}
