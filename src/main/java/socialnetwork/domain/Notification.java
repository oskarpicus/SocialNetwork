package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Notification extends Entity<Long>{
    private final String text;
    private final LocalDateTime date;
    private final List<Long> receivers;
    private static Long NUMBER_OF_NOTIFICATIONS = 1L;

    public Notification(String text) {
        this.text = text;
        date = LocalDateTime.now();
        receivers = new ArrayList<>();
        super.setId((NUMBER_OF_NOTIFICATIONS++));
    }

    public Notification(String text, LocalDateTime date, List<Long> receivers) {
        this.text = text;
        this.date = date;
        this.receivers = receivers;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Long> getReceivers() {
        return receivers;
    }

    public static void setNumberOfNotifications(Long numberOfNotifications) {
        NUMBER_OF_NOTIFICATIONS = numberOfNotifications;
    }
}
