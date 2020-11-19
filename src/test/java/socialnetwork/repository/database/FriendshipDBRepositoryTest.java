package socialnetwork.repository.database;

import org.junit.Test;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.FriendshipValidator;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class FriendshipDBRepositoryTest {

    FriendshipDBRepository friendshipDBRepository = new FriendshipDBRepository(new FriendshipValidator(),"social_network_test");

    @Test
    public void getSaveCommand() {
        Friendship friendship = new Friendship();
        friendship.setId(new Tuple<>(4L,2L));
        friendship.setDate(LocalDateTime.parse("2020-01-02T11:50:55"));
        assertEquals("INSERT INTO Friendships(id1,id2,date) VALUES (4,2,'2020-01-02');",friendshipDBRepository.getSaveCommand(friendship));
    }

    @Test
    public void getDeleteCommand() {
        assertEquals("DELETE FROM Friendships WHERE id1=4 AND id2=2;",friendshipDBRepository.getDeleteCommand(new Tuple<>(4L,2L)));
    }

    @Test
    public void getFindOneCommand() {
        assertEquals("SELECT * FROM Friendships WHERE id1=4 AND id2=2;",friendshipDBRepository.getFindOneCommand(new Tuple<>(4L,2L)));
    }

    @Test
    public void getFindAllCommand() {
        assertEquals("SELECT * FROM Friendships;",friendshipDBRepository.getFindAllCommand());
    }

}