package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message extends Entity<Long> {

    private final Long from;
    private final List<Long> to;
    private final String message;
    private LocalDateTime date;
    private static Long NUMBER_OF_MESSAGES = 1L;
    private final List<Long> reply = new ArrayList<>(); //ids of the reply messages
    private Long lastReplied = null; //id of the user that last replied

    public Message(Long from, List<Long> to, String message) {
        this.setId((NUMBER_OF_MESSAGES++));
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = LocalDateTime.now();
    }

    public static void setNUMBER_OF_MESSAGES(Long nr) {
        NUMBER_OF_MESSAGES = nr;
    }

    public void addToUser(Long u){
        this.to.add(u);
    }

    public void setDate(LocalDateTime date){
        this.date=date;
    }

    public void addReply(Long reply) {
        this.reply.add(reply);
    }

    public Long getFrom() {
        return from;
    }

    public List<Long> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Long> getReply() {
        return reply;
    }

    public Long getLastReplied() {
        return lastReplied;
    }

    public void setLastReplied(Long lastReplied) {
        this.lastReplied = lastReplied;
    }
}
