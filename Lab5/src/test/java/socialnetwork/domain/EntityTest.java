package socialnetwork.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntityTest {

    @Test
    public void getId() {
        Entity<Integer> entity = new Entity<>();
        entity.setId(4);
        assertEquals(Integer.valueOf(4),entity.getId());
    }

    @Test
    public void setId() {
        Entity<Long> entity = new Entity<>();
        entity.setId((long) 12000);
        assertEquals(Long.valueOf(12000),entity.getId());
    }
}