package socialnetwork.domain.dtos;

import socialnetwork.domain.User;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;

public class MessageDTO {

    private final Long messageId;
    private final User from;
    private final String text;
    private final LocalDateTime date;

    public MessageDTO(Long messageId, User from, String text, LocalDateTime date) {
        this.messageId = messageId;
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

    public Long getMessageId() {
        return messageId;
    }

    @Override
    public String toString() {
        return from.getFirstName()+" "+from.getLastName()+" wrote on "+ Constants.DATE_TIME_FORMATTER.format(date)+"\n\t"+text;
    }
}
