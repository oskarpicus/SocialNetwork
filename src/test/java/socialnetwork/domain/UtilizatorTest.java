package socialnetwork.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilizatorTest {

    @Test
    public void getFirstName() {
        User user = new User("ana","pop");
        assertEquals("ana",user.getFirstName());
    }

    @Test
    public void setFirstName() {
        User user = new User("ana","pop");
        user.setFirstName("ion");
        assertEquals("ion",user.getFirstName());
    }

    @Test
    public void getLastName() {
        User user = new User("ana","pop");
        assertEquals("pop",user.getLastName());
    }

    @Test
    public void setLastName() {
        User user = new User("ana","pop");
        user.setLastName("dumitru");
        assertEquals("dumitru",user.getLastName());
    }

    @Test
    public void getFriends() {
    }
}