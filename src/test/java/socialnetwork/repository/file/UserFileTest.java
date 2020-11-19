package socialnetwork.repository.file;

import org.junit.Test;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.domain.validators.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class UserFileTest {

    private final String fileName="data/test/usersTest.csv";
    private final Validator<User> validator = new UserValidator();
    UserFile userFile = new UserFile(fileName,validator);

    @Test
    public void save() {
        User user1 = new User("a","b");
        userFile.save(user1);

        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            String lastLine = lines.get(lines.size()-1);
            assertEquals(userFile.createEntityAsString(user1),lastLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
        userFile.delete(user1.getId());
    }

    @Test
    public void delete() {
        User user;
        if(userFile.findOne(1L).isEmpty())
            fail();
        user = userFile.findOne(1L).get();
        userFile.delete(user.getId());
        String userAsString = userFile.createEntityAsString(user);

        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> assertNotEquals(line,userAsString));
        } catch (IOException e) {
            e.printStackTrace();
        }

        userFile.save(user);
    }

    @Test
    public void update(){
        if(userFile.findOne(1L).isEmpty())
            fail();
        User oldUser = userFile.findOne(1L).get();
        User newUser = new User("ana","pop");
        newUser.setId(oldUser.getId());

        String newUserAsString = userFile.createEntityAsString(newUser);
        userFile.update(newUser);

        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            AtomicBoolean ok = new AtomicBoolean(false);
            lines.forEach(line -> {
                if(line.equals(newUserAsString))
                    ok.set(true);
            });
            if(!ok.get())
                fail();
        } catch (IOException e) {
            e.printStackTrace();
        }

        userFile.update(oldUser);

    }

    @Test
    public void extractEntity() {
        List<String> list = new ArrayList<>();
        list.add("1"); list.add("ana"); list.add("pop");
        User user = userFile.extractEntity(list);

        User correctUser = new User("ana","pop");
        correctUser.setId(1L);

        assertEquals(correctUser.getFirstName(),user.getFirstName());
        assertEquals(correctUser.getLastName(),user.getLastName());
    }

    @Test
    public void createEntityAsString() {
        User user = new User("ana","pop");
        String s = user.getId() +";ana;pop";
        assertEquals(s,userFile.createEntityAsString(user));
    }
}