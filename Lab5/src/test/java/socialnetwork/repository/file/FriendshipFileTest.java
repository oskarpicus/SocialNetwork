package socialnetwork.repository.file;

import org.junit.Test;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FriendshipFileTest {

    private final String filename = "data/test/friendshipTest.csv";
    private final Validator<Friendship> validator = new FriendshipValidator();
    private final FriendshipFile friendshipFile = new FriendshipFile(filename,validator);

    @Test
    public void extractEntity() {
        String line = "1;4;2020-01-02";
        List<String> attributes = Arrays.asList(line.split(";"));
        Friendship friendship = friendshipFile.extractEntity(attributes);
        Friendship correct = new Friendship();
        correct.setId(new Tuple<>(1L, 4L));
        assertEquals(correct.getId(),friendship.getId());
    }

    @Test
    public void createEntityAsString() {
        Friendship friendship = new Friendship();
        friendship.setId(new Tuple<>(1L,4L));
        friendship.setDate(LocalDateTime.parse("2018-05-05T11:50:55"));
        assertEquals("1;4;2018-05-05",friendshipFile.createEntityAsString(friendship));
    }
}