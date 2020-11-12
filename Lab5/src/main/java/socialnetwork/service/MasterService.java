package socialnetwork.service;

import socialnetwork.domain.*;

import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MasterService {

    private final FriendshipService friendshipService;
    private final UserService userService;
    private boolean updatedFriends = false;
    private final FriendRequestService friendRequestService;

    public MasterService(FriendshipService friendshipService, UserService userService, FriendRequestService friendRequestService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.friendRequestService = friendRequestService;
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

        //deleting all of the user's friend requests
        Predicate<FriendRequest> friendRequestPredicate = friendRequest ->
                friendRequest.getToUser().equals(id) || friendRequest.getFromUser().equals(id);
        List<FriendRequest> allRequests = this.friendRequestService.findAll();
        allRequests.forEach(friendRequest -> {
            if(friendRequestPredicate.test(friendRequest)){
                this.friendRequestService.remove(friendRequest.getId());
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
        if(id.getLeft().compareTo(id.getRight())>0)
            id=new Tuple<>(id.getRight(),id.getLeft());
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
     * Method for obtaining all the friend requests
     * @return list : List<FriendRequest>, which stores all the saved requests
     */
    public List<FriendRequest> getAllFriendRequests(){
        return this.friendRequestService.findAll();
    }

    /**
     * Method for adding a new friendship
     * @param friendship : Friendship, user to be added
     * @return an {@code Optional} - null if the friendship was saved,
     *                             - the user (id already exists)
     */
    public Optional<Friendship> addFriendship(Friendship friendship){
        Tuple<Long,Long> ids = friendship.getId();
        if(ids.getLeft().compareTo(ids.getRight())>0) { // we sort the tuple
            ids = new Tuple<>(ids.getRight(), ids.getLeft());
            friendship.setId(ids);
        }
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

    /**
     * Method for filtering friendships, based on a user ID
     * @param userID : Long, ID of the user
     * @return List<FriendshipDTO>, contains all the friendships of userID
     */
    public List<FriendshipDTO> filterFriendshipsID(Long userID){
        Predicate<Friendship> predicate = friendship ->
                friendship.getId().getLeft().equals(userID) ||
                friendship.getId().getRight().equals(userID);
        return filterFriendships(userID,predicate);
    }

    /**
     * Method for filtering friendships, based on a user ID and a month
     * @param userID : Long, ID of the user
     * @param month : Month
     * @return List<FriendshipDTO>, contains all the friendships of userID
     */
    public List<FriendshipDTO> filterFriendshipsIDMonth(Long userID, Month month){
        Predicate<Friendship> predicateUser = friendship ->
                friendship.getId().getLeft().equals(userID) || friendship.getId().getRight().equals(userID);
        Predicate<Friendship> predicateUserMonth = predicateUser.and(friendship ->
                friendship.getDate().getMonth().equals(month));
        return filterFriendships(userID,predicateUserMonth);
    }

    /**
     * Generic method for filtering Friendships of one User and based on further conditions
     * @param userID : Long, ID of a User
     * @param predicate : Predicat<Friendships>, the further conditions to be met
     * @return List<FriendshipDTO>, contains all the entries that are correct
     */
    private List<FriendshipDTO> filterFriendships(Long userID, Predicate<Friendship> predicate){
        return this.friendshipService.findAll().stream()
                .filter(predicate)
                .map(friendship -> {
                    Long id = friendship.getId().getLeft().equals(userID) ?
                            friendship.getId().getRight() :
                            friendship.getId().getLeft();
                    Optional<User> user = this.userService.findOne(id);
                    if(user.isEmpty())
                        return null;
                    return new FriendshipDTO(user.get().getFirstName(), user.get().getLastName(), friendship.getDate());
                })
                .collect(Collectors.toList());
    }

    /**
     * Method for sending a friend request
     * @param fromId : Long, ID of the user that sends the request
     * @param toId : Long, ID of the user that receives the request
     * @return an {@code Optional} - null if the FriendRequest was sent,
     *                                   - the FriendRequest (id already exists)
     * @throws ServiceException if
     *          - there are not such users with IDs fromId or toId
     *          - fromId and toId are already friends
     *          - the friend request was already sent
     */
    public Optional<FriendRequest> sendFriendRequest(Long fromId,Long toId){

        //we verify that the IDs refer actual users
        Optional<User> user1 = this.userService.findOne(fromId);
        if(user1.isEmpty())
            throw new ServiceException(fromId+" does not refer a user");

        //TODO add validator - functie separata pentru fiecare validare

        Optional<User> user2 = this.userService.findOne(toId);
        if(user2.isEmpty())
            throw new ServiceException(toId+"  does not refer a user");

        //we verify that there is no friendship between these users
        Tuple<Long,Long> ids = (fromId < toId) ? new Tuple<>(fromId,toId) : new Tuple<>(toId,fromId);
        Optional<Friendship> friendship = this.friendshipService.findOne(ids);
        if(friendship.isPresent())
            throw new ServiceException(fromId+" and "+toId+" are already friends");

        //we verify is the friend request was already sent (either in the same form, or inverse)
        Predicate<FriendRequest> predicateInverse = friendRequest -> friendRequest.getFromUser().equals(toId) &&
                friendRequest.getToUser().equals(fromId);
        Predicate<FriendRequest> predicate = predicateInverse.or(friendRequest ->
                        friendRequest.getFromUser().equals(fromId) &&
                                friendRequest.getToUser().equals(toId) &&
                                friendRequest.getStatus().equals("pending")
                );
        List<FriendRequest> list = this.friendRequestService.findAll();
      //  if(list.stream().anyMatch(predicate))
        //    throw new ServiceException("The friend request was already sent");

        return this.friendRequestService.add(new FriendRequest(fromId,toId));
    }

    /**
     * Method for accepting a friend request
     * @param id : Long, id of the friend request to be accepted
     * @return an {@code Optional}
     *                    - empty, if it was successfully accepted
     *                    - otherwise, the entity
     */
    public Optional<FriendRequest> acceptFriendRequest(Long id){
       Optional<FriendRequest> result = this.friendRequestService.acceptFriendRequest(id);
       if(result.isEmpty()){
           // accepted successfully ==> we add the friendships
           Optional<FriendRequest> request = this.friendRequestService.findOne(id);
           request.ifPresent(
                   friendRequest -> {
                       Long fromId=friendRequest.getFromUser();
                       Long toId=friendRequest.getToUser();
                       Tuple<Long,Long> ids = (fromId < toId) ? new Tuple<>(fromId,toId) : new Tuple<>(toId,fromId);
                       Friendship friendship = new Friendship();
                       friendship.setId(ids);
                       this.friendshipService.add(friendship);
                   }
           );
       }
       return result;
    }

    /**
     * Method for rejecting a friend request
     * @param id : Long, id of the friend request to be rejected
     * @return an {@code Optional}
     *              - empty, if it was successfully rejected
     *              - otherwise, the entity
     */
    public Optional<FriendRequest> rejectFriendRequest(Long id){
        return this.friendRequestService.rejectFriendRequest(id);
    }

}
