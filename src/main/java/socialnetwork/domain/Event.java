package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event extends Entity<Long>{

    private final String name;
    private final List<Long> participants;
    private final LocalDateTime date;
    private final String description;
    private final String location;
    private final List<Long> subscribedToNotification;
    private final List<Long> receivedNotification;
    private static long NUMBER_OF_EVENTS = 1L;

    public Event(String name, LocalDateTime date, String description, String location) {
        super.setId((NUMBER_OF_EVENTS++));
        this.name = name;
        this.date = date;
        this.description = description;
        this.location = location;
        participants = new ArrayList<>();
        subscribedToNotification = new ArrayList<>();
        receivedNotification = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public List<Long> getSubscribedToNotification() {
        return subscribedToNotification;
    }

    public List<Long> getReceivedNotification() {
        return receivedNotification;
    }

    public static void setNumberOfEvents(long numberOfEvents) {
        NUMBER_OF_EVENTS = numberOfEvents;
    }
}
