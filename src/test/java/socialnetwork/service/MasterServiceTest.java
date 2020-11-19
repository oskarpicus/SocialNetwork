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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class MasterServiceTest {

    private final String fileNameFriendships = "data/test/friendshipTest.csv";
    private final Repository<Tuple<Long,Long>, Friendship> repo = new FriendshipFile(fileNameFriendships,
            new FriendshipValidator());
    private final String fileNameUsers = "data/test/usersTest.csv";
    private final Repository<Long, User> userRepository = new UserFile(fileNameUsers,
            new UserValidator());
    private final FriendshipService friendshipService = new FriendshipService(repo,userRepository);
    private final UserService userService = new UserService(userRepository);
    private final MasterService masterService = new MasterService(friendshipService,userService, friendRequestService);

    @Test
    public void addUser() {
        User user = new User("a","b");
        user.setId(2L);
        assertTrue(masterService.addUser(user).isPresent()); //it was not saved
        user.setId(7331115341259248461L);
        assertTrue(masterService.addUser(user).isEmpty()); // it was saved
        assertTrue(masterService.getAllUsers().contains(user));
        masterService.removeUser(7331115341259248461L);
    }

    @Test
    public void removeUser() {
        Friendship friendship = new Friendship();
        friendship.setId(new Tuple<>(1L,4L));
        User user = new User("Barbu","Andrei");
        user.setId(7331115341259248461L);
        assertEquals(Optional.empty(),masterService.removeUser(7331115341259248461L)); //it was not removed

        user.setId(4L);
        assertTrue(masterService.removeUser(4L).isPresent());

        // we check if user's friendships were deleted
        assertFalse(friendshipService.findAll().contains(friendship));

        userRepository.save(user);
        friendshipService.add(friendship);
    }

    @Test
    public void removeFriendship() {
        //we add a new friendship
        Friendship friendship = new Friendship();
        friendship.setId(new Tuple<>(4L,2L));
        assertEquals(Optional.empty(),masterService.addFriendship(friendship));
        assertTrue(masterService.removeFriendship(new Tuple<>(4L,2L)).isPresent());

        // we check if the friendship was saved in the users' lists
        Optional<User> userOptional2 = userRepository.findOne(2L);
        assertTrue(userOptional2.isPresent());
        Optional<User> userOptional4 = userRepository.findOne(4L);
        assertTrue(userOptional4.isPresent());

        assertFalse(userOptional2.get().getFriends().contains(userOptional4.get()));
        assertFalse(userOptional4.get().getFriends().contains(userOptional2.get()));

        assertEquals(Optional.empty(),masterService.addFriendship(friendship));
        assertTrue(masterService.removeFriendship(new Tuple<>(4L,2L)).isPresent());

    }

    @Test
    public void getAllUsers() {
        assertEquals(5,masterService.getAllUsers().size());
    }

    @Test
    public void addFriendship() {
        Friendship friendship = new Friendship();
        friendship.setId(new Tuple<>(4L,2L));
        assertEquals(Optional.empty(),masterService.addFriendship(friendship));
        Optional<User> userOptional2 = userRepository.findOne(2L);
        assertTrue(userOptional2.isPresent());
        Optional<User> userOptional4 = userRepository.findOne(4L);
        assertTrue(userOptional4.isPresent());

        // we check if the friendship was saved in the users' lists
        assertTrue(userOptional2.get().getFriends().contains(userOptional4.get()));
        assertTrue(userOptional4.get().getFriends().contains(userOptional2.get()));
        masterService.removeFriendship(friendship.getId());
    }

    @Test
    public void getNumberOfCommunities() {

        assertEquals(4,masterService.getNumberOfCommunities());
        Friendship friendship = new Friendship();
        friendship.setId(new Tuple<>(2L,3L));
        masterService.addFriendship(friendship);

        assertEquals(3,masterService.getNumberOfCommunities());

        friendship.setId(new Tuple<>(1L,2L));
        masterService.addFriendship(friendship);
        assertEquals(2,masterService.getNumberOfCommunities());

        User user = new User("Iancu","Mihaela");
        user.setId(2L);

        masterService.removeUser(2L);
        assertEquals(3,masterService.getNumberOfCommunities());

        User userSaved = new User("a","b");
        userSaved.setId(7331115341259248461L);
        masterService.addUser(userSaved);
        assertEquals(4,masterService.getNumberOfCommunities());

        masterService.addUser(user);
        masterService.removeUser(userSaved.getId());
        masterService.removeFriendship(new Tuple<>(1L,2L));
        masterService.removeFriendship(new Tuple<>(2L,3L));
    }

    @Test
    public void getMostSociable(){
        List<User> result = this.masterService.getMostSociable();
        assertEquals(2,result.size());
        User user = result.get(0);
        assertTrue(user.getId().equals(1L) || user.getId().equals(4L));
        user = result.get(1);
        assertTrue(user.getId().equals(1L) || user.getId().equals(4L));

        //we add friendships
        Friendship friendship = new Friendship();
        friendship.setId(new Tuple<>(2L,3L));
        this.masterService.addFriendship(friendship);
        friendship.setId(new Tuple<>(3L,5L));
        this.masterService.addFriendship(friendship);

        result = this.masterService.getMostSociable();
        assertEquals(3,result.size());
        user = result.get(0);
        assertTrue(user.getId().equals(2L) || user.getId().equals(3L) || user.getId().equals(5L));
        user = result.get(1);
        assertTrue(user.getId().equals(2L) || user.getId().equals(3L) || user.getId().equals(5L));
        user = result.get(2);
        assertTrue(user.getId().equals(2L) || user.getId().equals(3L) || user.getId().equals(5L));

        friendship.setId(new Tuple<>(4L,5L));
        this.masterService.addFriendship(friendship);
        result = this.masterService.getMostSociable();
        assertEquals(5,result.size());

        masterService.removeFriendship(new Tuple<>(2L,3L));
        masterService.removeFriendship(new Tuple<>(3L,5L));
        masterService.removeFriendship(new Tuple<>(4L,5L));
    }
}