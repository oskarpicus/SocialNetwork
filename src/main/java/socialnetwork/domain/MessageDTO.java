package socialnetwork.domain;

import java.time.LocalDateTime;

public class MessageDTO {

    private final User from;
    private final String text;
    private final LocalDateTime date;

    public MessageDTO(User from , String text, LocalDateTime date) {
        this.from = from;
        this.text = text;
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
