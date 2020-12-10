package socialnetwork.domain.dtos;

import socialnetwork.domain.Tuple;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;

public class FriendshipDTO {

    private final String firstName;
    private final String lastName;
    private final LocalDateTime date;
    private final Tuple<Long,Long> ids;
    private final String dateAsString;

    public FriendshipDTO(String firstName, String lastName, LocalDateTime date, Tuple<Long, Long> ids) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.ids = ids;
        this.dateAsString= Constants.DATE_TIME_FORMATTER.format(date);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Tuple<Long, Long> getIds() {
        return ids;
    }

    public String getDateAsString() {
        return dateAsString;
    }

    @Override
    public String toString() {
        return firstName+" "+lastName+" added on\n"+dateAsString;
    }
}
