package socialnetwork.domain;

import java.time.LocalDateTime;


public class Friendship extends Entity<Tuple<Long,Long>> {

    public void setDate() {
        this.date = LocalDateTime.now();
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    LocalDateTime date;

    public Friendship() {
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id1= "+getId().getLeft()+
                "id2= "+getId().getRight()+
             //   "date=" + date +
                '}';
    }
}

