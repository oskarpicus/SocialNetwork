package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MasterService {

    private final FriendshipService friendshipService;
    private final UserService userService;

    public MasterService(FriendshipService friendshipService, UserService userService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
    }

    public Optional<User> addUser(User user){
        return this.userService.add(user);
    }

    public Optional<User> removeUser(Long id){
        Optional<User> result = this.userService.remove(id);

        if(result.isEmpty())
            return result;

        Predicate<Friendship> friendshipPredicate = friendship -> friendship.getId().getLeft().equals(id)
                || friendship.getId().getRight().equals(id);

        List<Friendship> allFriendships = this.friendshipService.findAll();
        allFriendships.forEach(friendship -> {
            if(friendshipPredicate.test(friendship)){
                this.friendshipService.remove(friendship.getId());
            }
        });

        return result;
    }


    public Optional<Friendship> removeFriendship(Tuple<Long,Long> id){
        return this.friendshipService.remove(id);
    }

    public List<User> getAllUsers(){
        return this.userService.getAllUsers();
    }

    public Optional<Friendship> addFriendship(Friendship friendship){
        Optional<Friendship> result = this.friendshipService.add(friendship);
        if(result.isPresent()) //if the friendship was not saved
            return result;

        //we update each user's friend list
        Tuple<Long,Long> ids = friendship.getId();
        Optional<User> user1 = this.userService.findOne(ids.getLeft());
        Optional<User> user2 = this.userService.findOne(ids.getRight());

        if(user1.isPresent() && user2.isPresent()){
            user1.get().addFriend(user2.get());
            user2.get().addFriend(user1.get());
        }

        return result;
    }

    public int getNumberOfCommunities(){
        return this.friendshipService.getNumberOfCommunities();
    }
}
