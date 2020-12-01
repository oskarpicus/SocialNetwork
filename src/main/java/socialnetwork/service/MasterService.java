package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.domain.dtos.FriendshipDTO;
import socialnetwork.domain.dtos.MessageDTO;
import socialnetwork.domain.validators.FriendRequestVerifier;
import socialnetwork.domain.validators.MessageVerifier;

import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MasterService {

    private final FriendshipService friendshipService;
    protected final UserService userService;
    private boolean updatedFriends;
    protected final FriendRequestService friendRequestService;
    private final FriendRequestVerifier friendRequestVerifier;
    private final MessageService messageService;
    private final MessageVerifier messageVerifier;

    public MasterService(FriendshipService friendshipService, UserService userService, FriendRequestService friendRequestService,MessageService messageService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.friendRequestService = friendRequestService;
        friendRequestVerifier = new FriendRequestVerifier(friendshipService,userService,friendRequestService);
        this.messageService=messageService;
        this.messageVerifier=new MessageVerifier(userService,messageService);

        updateAllUsersFriends();
        updatedFriends=true;
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
        this.updateOnUserDeleted(result.get());
        return result;
    }

    /**
     * Method for updating the other entities upon the deletion of a user
     * @param userDeleted : User, the user that was deleted
     * The friendships, messages and friend requests of user are deleted
     */
    private void updateOnUserDeleted(User userDeleted){
        //deleting in cascade all the friendships that belong to the user
        Predicate<Friendship> friendshipPredicate = friendship -> friendship.getId().getLeft().equals(userDeleted.getId())
                || friendship.getId().getRight().equals(userDeleted.getId());

        List<Friendship> allFriendships = this.friendshipService.findAll();
        allFriendships.forEach(friendship -> {
            if(friendshipPredicate.test(friendship)){
                this.friendshipService.remove(friendship.getId());
            }
        });

        //deleting all of the user's appearances in the other users' friends list
        Predicate<User> userPredicate = user -> user.getFriends().contains(userDeleted);
        this.userService.findAll().forEach(user -> {
            if(userPredicate.test(user)){
                user.getFriends().remove(userDeleted);
            }
        });

        //deleting all of the user's friend requests
        Predicate<FriendRequest> friendRequestPredicate = friendRequest ->
                friendRequest.getToUser().equals(userDeleted.getId()) || friendRequest.getFromUser().equals(userDeleted.getId());
        List<FriendRequest> allRequests = this.friendRequestService.findAll();
        allRequests.forEach(friendRequest -> {
            if(friendRequestPredicate.test(friendRequest)){
                this.friendRequestService.remove(friendRequest.getId());
            }
        });

        //delete the messages that the user sent
        List<Message> allMessages = this.messageService.findAll();
        allMessages.forEach(message -> {
            if(message.getFrom().equals(userDeleted.getId()) || message.getTo().contains(userDeleted.getId()))
                this.messageService.remove(message.getId());
        });
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
        return this.userService.findAll();
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
        this.friendRequestVerifier.validate(fromId,toId);
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


    public Optional<Message> sendMessage(Message message){
        return this.messageService.add(message);
    }

    /**
     *
     * @param messageId : Long, id of the message to reply to
     * @param userToId : Long, id of the user that replies to the message
     * @param text : String, the content of the message
     * @return an {@code Optional}
     *              - empty, if the messages was replied successfully
     *              - otherwise, return the message
     */
    public Optional<Message> replyMessage(Long messageId,Long userToId,String text){

        messageVerifier.verifyForReply(messageId,userToId);
        Optional<Message> message = this.messageService.findOne(messageId);
        if(message.isEmpty())
            throw new ServiceException("Message does not exist");

        //we add the reply message
        Long destination=message.get().getFrom();
        Message replyMessage = new Message(userToId, Arrays.asList(destination),text);
        Optional<Message> result= this.messageService.add(replyMessage);

        //we update the message that was replied
        message.get().addReply(replyMessage.getId());
        message.get().setLastReplied(userToId);
        this.messageService.update(message.get());

        return result;
    }

    /**
     * Method for obtaining the entire conversation between users
     * @param id1 : Long, id of one user
     * @param id2 : Long, id of another user
     * @return List<MessageDTO>, contains all the messages between id1 and id2
     */
    public List<MessageDTO> getConversation(Long id1, Long id2){
        Predicate<Message> predicate = message -> (message.getFrom().equals(id1)
                && message.getTo().contains(id2)) || (message.getFrom().equals(id2)
                && message.getTo().contains(id1));

        User user1 = this.messageVerifier.userExists(id1);
        User user2 = this.messageVerifier.userExists(id2);

        return this.messageService.findAll()
                .stream()
                .filter(predicate)
                .sorted(Comparator.comparing(Message::getDate))
                .map(message -> {
                    User user = message.getFrom().equals(id1) ? user1 : user2;
                    return new MessageDTO(user, message.getMessage(), message.getDate());
                })
                .collect(Collectors.toList());
    }

    /**
     * Method for finding a user by their id
     * @param id : Long , id of the user
     * @return an {@code Optional}
     *              - the user, if id refers a user
     *              - null , if there is no user with that id
     */
    public Optional<User> findOneUser(Long id){
        return this.userService.findOne(id);
    }

    public User logging(String firstName, String lastName, Long id){
        return null;
    }

}
