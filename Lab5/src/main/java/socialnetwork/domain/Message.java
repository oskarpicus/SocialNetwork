package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {

    private final User from;
    private final List<User> to;
    private final String message;
    private final LocalDateTime date;


    public Message(User from, List<User> to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = LocalDateTime.now();
    }

    public User getFrom() {
        return from;
    }

    public List<User> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
