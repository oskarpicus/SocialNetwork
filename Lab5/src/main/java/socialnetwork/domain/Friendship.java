package socialnetwork.domain;

import java.time.LocalDateTime;


public class Friendship extends Entity<Tuple<Long,Long>> {

    public void setDate() {
        this.date = LocalDateTime.now();
    }

    LocalDateTime date;

    public Friendship() {
        setDate();
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }
}
