package socialnetwork.repository.memory;

import org.junit.Test;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.domain.validators.ValidationException;

import java.util.Optional;

import static org.junit.Assert.*;

public class InMemoryRepositoryTest {

    private final InMemoryRepository<Long,User> repo = new InMemoryRepository<>(new UserValidator());

    @Test
    public void findOne() {
        User user1 = new User("a","b");
        repo.save(user1);
        User user2 = new User("a","b");
        repo.save(user2);
        try{
            repo.findOne(null);
            fail();
        }catch(IllegalArgumentException e){
            assertEquals("id must not be null",e.getMessage());
        }
        assertEquals(Optional.empty(),repo.findOne(7331115341259248461L));
        assertEquals(Optional.of(user1),repo.findOne((user1.getId())));
    }

    @Test
    public void findAll() {
    }

    @Test
    public void save() {
        User user1 = new User("a","b");
        repo.save(user1);
        assertEquals(Optional.of(user1),repo.findOne(user1.getId()));

        try{
            repo.save(new User("",""));
            fail();
        }catch (ValidationException e){
            assertEquals("Invalid First Name\nInvalid Last Name\n",e.getMessage());
        }

        try{
            repo.save(null);
            fail();
        }catch (IllegalArgumentException e){
            assertEquals("entity must not be null",e.getMessage());
        }

    }

    @Test
    public void delete() {
        try{
            repo.delete(null);
            fail();
        }catch (IllegalArgumentException e){
            assertEquals("id must not be null",e.getMessage());
        }

        User user1 = new User("a","b");
        repo.save(user1);
        assertEquals(Optional.empty(),repo.delete(7331115341259248461L));
        assertEquals(Optional.of(user1),repo.delete(user1.getId()));
    }

    @Test
    public void update() {
        try{
            repo.update(null);
            fail();
        }catch (IllegalArgumentException e){
            assertEquals("entity must not be null",e.getMessage());
        }

        try{
            repo.update(new User("",""));
            fail();
        }catch (ValidationException e){
            assertEquals("Invalid First Name\nInvalid Last Name\n",e.getMessage());
        }

        User user1 = new User("a","b");
        repo.save(user1);
        User user2 = new User("b","c");
        assertEquals(Optional.of(user2),repo.update(user2));
        user1.setFirstName("Ion");
        assertEquals(Optional.empty(),repo.update(user1));
        assertEquals(user1,repo.findOne(user1.getId()).get());
    }
}