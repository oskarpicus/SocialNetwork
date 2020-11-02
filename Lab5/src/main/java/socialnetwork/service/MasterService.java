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
    private boolean updatedFriends = false;

    public MasterService(FriendshipService friendshipService, UserService userService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
    }

    /**
     * Method for adding a new user
     * @param user : User, user to be added
     * @return an {@code Optional} - null if the user was saved,
     *                             - the user (id already exists)
     */
    public Optional<User> addUser(User user){
        return this.userService.add(user);
    }

    /**
     *  removes the USer with the specified id
     * @param id
     *      id must be not null
     * @return an {@code Optional}
     *            - null if there is no entity with the given id,
     *            - the removed entity, otherwise
     */
    public Optional<User> removeUser(Long id){
        Optional<User> result = this.userService.remove(id);

        if(result.isEmpty())
            return result;

        //deleting in cascade all the friendships that belong to the user
        Predicate<Friendship> friendshipPredicate = friendship -> friendship.getId().getLeft().equals(id)
                || friendship.getId().getRight().equals(id);

        List<Friendship> allFriendships = this.friendshipService.findAll();
        allFriendships.forEach(friendship -> {
            if(friendshipPredicate.test(friendship)){
                this.friendshipService.remove(friendship.getId());
            }
        });

        //deleting all of the user's appearances in the other users' friends list
        Predicate<User> userPredicate = user -> user.getFriends().contains(result.get());
        this.userService.getAllUsers().forEach(user -> {
            if(userPredicate.test(user)){
                user.getFriends().remove(result.get());
            }
        });


        return result;
    }


    /**
     *  removes the friendship with the specified id
     * @param id
     *      id must be not null
     * @return an {@code Optional}
     *            - null if there is no entity with the given id,
     *            - the removed entity, otherwise
     */
    public Optional<Friendship> removeFriendship(Tuple<Long,Long> id){
        Optional<Friendship> result1 = this.friendshipService.remove(id);
        if(result1.isPresent()){
            deleteOneUsersFriends(id);
        }
        return result1;
    }

    /**
     * Method for obtaining all the saved users
     * @return list : List<User>, which stores all the saved users
     */
    public List<User> getAllUsers(){
        return this.userService.getAllUsers();
    }

    /**
     * Method for adding a new friendship
     * @param friendship : Friendship, user to be added
     * @return an {@code Optional} - null if the friendship was saved,
     *                             - the user (id already exists)
     */
    public Optional<Friendship> addFriendship(Friendship friendship){
        Optional<Friendship> result = this.friendshipService.add(friendship);
        if(result.isPresent()) //if the friendship was not saved
            return result;

        if(!addOneUsersFriends(friendship)){
            throw new ServiceException("Users with those IDs don't exist\n");
        }

        return result;
    }

    /**
     * Method for obtaining the number of communities in the social network
     * @return nr : int, represents the total number of communities
     */
    public int getNumberOfCommunities(){
        if(!updatedFriends){
            updateAllUsersFriends();
            updatedFriends=true;
        }
        return this.friendshipService.getNumberOfCommunities();
    }

    /**
     * Method for obtaining the most sociable community
     * ( the connected component with the longest path )
     * @return list : List<User>, contains all of the users in the most sociable community
     */
    public List<User> getMostSociable(){
        if(!updatedFriends){
            updateAllUsersFriends();
            updatedFriends=true;
        }
        return friendshipService.getMostSociable();
    }

    /**
     * Method for updating one user's friend list, based on a friendship
     * @param friendship : Friendship
     * @return true, if the update was successful, false, otherwise
     */
    private boolean addOneUsersFriends(Friendship friendship){
        Tuple<Long,Long> ids = friendship.getId();
        Optional<User> user1 = this.userService.findOne(ids.getLeft());
        Optional<User> user2 = this.userService.findOne(ids.getRight());
        if(user1.isPresent() && user2.isPresent()){
            if(user1.get().getFriends().contains(user2.get())) //if the friendship was already saved
                return true;

            user1.get().addFriend(user2.get());
            user2.get().addFriend(user1.get());
            return true;
        }
        return false;
    }

    /**
     * Method for updating every user's friend list, based on the current saved friendships
     */
    private void updateAllUsersFriends(){
        List<Friendship> allFriendships = this.friendshipService.findAll();
        for(Friendship friendship : allFriendships){
            if(!addOneUsersFriends(friendship)){
                throw new ServiceException("Users with those IDs don't exist\n");
            }
        }
    }

    /**
     * Method for updating users' friend list, after deleting a particular friendship
     * @param ids : ID of the friendship that was deleted
     * The friendship between the users with those ids is deleted from their lists, as well
     */
    private void deleteOneUsersFriends(Tuple<Long,Long> ids){
        Optional<User> user1 = this.userService.findOne(ids.getLeft());
        Optional<User> user2 = this.userService.findOne(ids.getRight());
        if(user1.isPresent() && user2.isPresent()){
            user1.get().getFriends().removeIf(user -> user.getId().equals(ids.getRight()));
            user2.get().getFriends().removeIf(user -> user.getId().equals(ids.getLeft()));
        }
    }

}
