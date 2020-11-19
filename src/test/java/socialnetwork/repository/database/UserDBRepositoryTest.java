package socialnetwork.repository.database;

import org.junit.Test;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.UserValidator;

import java.util.Optional;

import static org.junit.Assert.*;

public class UserDBRepositoryTest {

    UserDBRepository userDBRepository = new UserDBRepository(new UserValidator(),"social_network_test");

    @Test
    public void findOne() {
        assertTrue(userDBRepository.findOne(3L).isPresent());
        assertTrue(userDBRepository.findOne(1L).isPresent());
        assertTrue(userDBRepository.findOne(2L).isPresent());
        assertTrue(userDBRepository.findOne(3L).isPresent());
        assertTrue(userDBRepository.findOne(4L).isPresent());
        assertTrue(userDBRepository.findOne(124214L).isEmpty());
    }

    @Test
    public void save() {
        User user = new User("Luminita","Anghel");
        assertTrue(userDBRepository.save(user).isEmpty()); //saving doesn't fail
        assertTrue(userDBRepository.findOne(user.getId()).isPresent());
        userDBRepository.delete(user.getId());
    }

    @Test
    public void delete() {
//        assertEquals(Optional.empty(),userDBRepository.delete(9042422424L));
//        User user = new User("Luminita","Anghel");
//        userDBRepository.save(user);
//        assertTrue(userDBRepository.findOne(user.getId()).isPresent());
//        userDBRepository.delete(user.getId());
//        assertTrue(userDBRepository.findOne(user.getId()).isEmpty());
    }

    @Test
    public void update() {
    }

    @Test
    public void getSaveCommand() {
        User user = new User("Anghel","Luminita");
        assertEquals("INSERT INTO Users(firstName,secondName) VALUES ('Anghel', 'Luminita');",userDBRepository.getSaveCommand(user));
    }

    @Test
    public void getDeleteCommand() {
        assertEquals("DELETE FROM Users WHERE id =4;",userDBRepository.getDeleteCommand(4L));
    }

    @Test
    public void getFindOneCommand() {
        assertEquals("SELECT * FROM Users WHERE id =4;",userDBRepository.getFindOneCommand(4L));
    }

    @Test
    public void getFindAllCommand() {
        assertEquals("SELECT * FROM Users;",userDBRepository.getFindAllCommand());
    }

}