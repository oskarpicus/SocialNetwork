package socialnetwork.service;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.UserDTO;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MasterServiceWithLogging extends MasterService implements Observable {

    private User loggedUser;
    private Long loggedUserId;
    private List<UserDTO> allUsers = null;
    private List<Observer> observers = new ArrayList<>();;

    public MasterServiceWithLogging(FriendshipService friendshipService, UserService userService, FriendRequestService friendRequestService, MessageService messageService) {
        super(friendshipService, userService, friendRequestService, messageService);
    }

    /**
     * Method for logging the user in the application
     * @param userId : Long, ID of the user that wants to log
     * @throws ServiceException - if there is no user with ID userId
     */
    @Override
    public User logging(String firstName, String lastName, Long userId){
        super.findOneUser(userId).ifPresentOrElse(
                result -> loggedUser = result,
                () -> {
                    throw new ServiceException("Invalid credentials");
                }
        );
        if(loggedUser.getLastName().equals(lastName) && loggedUser.getFirstName().equals(firstName))
            return loggedUser;
        throw new ServiceException("Invalid credentials");
    }

    private void checkFriendRequestIsForLoggedUser(Long id){
        Optional<FriendRequest> request = super.friendRequestService.findOne(id);
        if(request.isEmpty() || !request.get().getToUser().equals(loggedUser.getId()))
            throw new ServiceException("This friend request was not sent to you");
    }

    @Override
    public Optional<FriendRequest> acceptFriendRequest(Long id) {
        checkFriendRequestIsForLoggedUser(id);
        Optional<FriendRequest> result = super.acceptFriendRequest(id);
        if(result.isEmpty()){
            //we update the DTOs list
            Optional<FriendRequest> request = super.friendRequestService.findOne(id);
            if(request.isPresent()){
                Long other = request.get().getFromUser();
                setFriendship(other,true);
            }
        }
        return result;
    }

    @Override
    public Optional<FriendRequest> rejectFriendRequest(Long id) {
        checkFriendRequestIsForLoggedUser(id);
        return super.rejectFriendRequest(id);
    }

    /**
     * Method for filtering a list of users that contain a certain string in their names
     * @param string : String
     * @return list of all the users that contain string in their names
     */
    public List<UserDTO> filterUsers(String string){
        return allUsers.stream()
                .filter(userDTO -> userDTO.getFirstName().contains(string) ||
                        userDTO.getLastName().contains(string))
                .collect(Collectors.toList());
    }

    /**
     * Method for obtaining all the users, specifying if they are friends with another user
     * @param idToBeFriendsWith : Long, id of the user to check if they are friends with
     * @return list of all the users
     */
    public List<UserDTO> getAllUserDTO(Long idToBeFriendsWith){
        if(allUsers==null)
        {
            loggedUserId = idToBeFriendsWith;
            allUsers = this.userService.findAll().stream()
                    .filter(user -> !user.getId().equals(idToBeFriendsWith))
                    .map(user -> {
                        boolean friendsWith = false;

                        if(user.getFriends().stream().anyMatch(user1 -> user1.getId().equals(idToBeFriendsWith)))
                            friendsWith=true;

                        return new UserDTO(user.getId(),user.getFirstName(), user.getLastName(),
                                friendsWith);
                    })
                    .collect(Collectors.toList());
        }
        return allUsers;
    }

    @Override
    public Optional<Friendship> removeFriendship(Tuple<Long, Long> id) {
        Optional<Friendship> result = super.removeFriendship(id);
        if(result.isPresent()){
            //we update the DTOs list
            Long otherId = result.get().getId().getLeft().equals(loggedUserId) ?
                    result.get().getId().getRight() :
                    result.get().getId().getLeft();
            setFriendship(otherId,false);
        }
        notifyObservers();
        return result;
    }

    private void setFriendship(Long user,boolean friendships){
        allUsers.stream()
                .filter(userDTO -> userDTO.getId().equals(user))
                .findAny()
                .ifPresent(userDTO -> {
                    userDTO.setFriendsWithLoggedUser(friendships);
                });
    }

    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
