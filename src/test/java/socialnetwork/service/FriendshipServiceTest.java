package socialnetwork.service;

import org.junit.Test;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.FriendshipFile;
import socialnetwork.repository.file.UserFile;

import java.util.Optional;

import static org.junit.Assert.*;

public class FriendshipServiceTest {

    private final String fileNameFriendships = "data/test/friendshipTest.csv";
    private final Repository<Tuple<Long,Long>, Friendship> repo = new FriendshipFile(fileNameFriendships,
            new FriendshipValidator());
    private final String fileNameUsers = "data/test/usersTest.csv";
    private final Repository<Long, User> userRepository = new UserFile(fileNameUsers,
            new UserValidator());
    private final FriendshipService friendshipService = new FriendshipService(repo,userRepository);

    @Test
    public void add() {

        Friendship friendship = new Friendship();
        friendship.setId(new Tuple<>(7331115341259248461L, 7331115341259248421L));

        try{
            friendshipService.add(friendship);
            fail();
        }catch (ServiceException e){
            assertEquals("Id 7331115341259248461 does not refer a user",e.getMessage());
        }

        friendship.setId(new Tuple<>(1L, 7331115341259248461L));
        try{
            friendshipService.add(friendship);
            fail();
        }catch (ServiceException e){
            assertEquals("Id 7331115341259248461 does not refer a user",e.getMessage());
        }

        friendship.setId(new Tuple<>(1L,4L));
        assertTrue(friendshipService.add(friendship).isPresent()); //it was not added, it already exists

        friendship.setId(new Tuple<>(3L,4L));
        assertEquals(Optional.empty(),friendshipService.add(friendship));
        assertTrue(friendshipService.findAll().contains(friendship));

        friendshipService.remove(friendship.getId());
    }

    @Test
    public void remove() {

        Tuple<Long,Long> ids = new Tuple<>(1L, 7331115341259248461L);
        assertEquals(Optional.empty(),friendshipService.remove(ids));
        ids.setRight(4L);
        assertTrue(friendshipService.remove(ids).isPresent()); //the removal was successful

        Friendship friendship = new Friendship();
        friendship.setId(ids);
        friendshipService.add(friendship);
    }


}