package socialnetwork.service;

import org.junit.Test;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.UserFile;

import java.util.Optional;

import static org.junit.Assert.*;

public class UserServiceTest {

    private final String fileNameUsers = "data/test/usersTest.csv";
    private final Repository<Long, User> userRepository = new UserFile(fileNameUsers,
            new UserValidator());
    private final UserService userService = new UserService(userRepository);

    @Test
    public void add() {
        User user = new User("a","b");
        user.setId(1L);
        assertTrue(userService.add(user).isPresent()); //it was not saved
        user.setId(7331115341259248461L);
        assertEquals(Optional.empty(),userService.add(user)); //it was saved
        assertTrue(userService.getAllUsers().contains(user));
        userRepository.delete(user.getId());
    }

    @Test
    public void getAllUsers() {
        assertEquals(5, userService.getAllUsers().size());
    }


    @Test
    public void remove() {
        assertEquals(Optional.empty(),userService.remove(7331115341259248461L));
        Optional<User> result = userService.remove(5L);
        assertTrue(result.isPresent());
        assertEquals(5L, (long) result.get().getId());
        User user = new User("Popa","Maria");
        user.setId(5L);
        userRepository.save(user);
    }

    @Test
    public void findOne(){
        assertFalse(userService.findOne(7331115341259248461L).isPresent());
        assertTrue(userService.findOne(1L).isPresent());
        Optional<User> result = userService.findOne(3L);
        assertTrue(result.isPresent());
        assertEquals(3L, (long) result.get().getId());
    }
}