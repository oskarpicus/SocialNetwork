package socialnetwork.service;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.FriendRequestDTO;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MasterServiceWithLogging extends MasterService implements Observable {

    private User loggedUser;
    private final List<Observer> observers = new ArrayList<>();
    private  List<FriendRequestDTO> allFriendRequests;

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
            //we update the DTOs lists
            Optional<FriendRequest> request = super.friendRequestService.findOne(id);
            if(request.isPresent()){
                Long other = request.get().getFromUser();
                //setFriendship(other,true);
                //TODO update the friends list
            }
            updateFriendRequestList(id,"accepted");
            notifyObservers();
        }
        return result;
    }

    @Override
    public Optional<FriendRequest> rejectFriendRequest(Long id) {
        checkFriendRequestIsForLoggedUser(id);
        Optional<FriendRequest> result= super.rejectFriendRequest(id);
        if(result.isEmpty()){
            //update the dto list
            updateFriendRequestList(id,"rejected");
            notifyObservers();
        }
        return result;
    }

    /**
     * Method for filtering a list of users that contain a certain string in their names
     * @param string : String
     * @return list of all the users that contain string in their names
     */
    public List<User> filterUsers(String string){
        return this.getAllUsers().stream()
                .filter(user -> user.getFirstName().contains(string) ||
                        user.getLastName().contains(string))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Friendship> removeFriendship(Tuple<Long, Long> id) {
        Optional<Friendship> result = super.removeFriendship(id);
        if(result.isPresent()){
            //we update the DTOs list
            Long otherId = result.get().getId().getLeft().equals(loggedUser.getId()) ?
                    result.get().getId().getRight() :
                    result.get().getId().getLeft();
            //setFriendship(otherId,false);
            //TODO when the user removes a friendships, update the tables
        }
        notifyObservers();
        return result;
    }

    public List<FriendRequestDTO> getAllFriendRequestsDTO(){
        return (allFriendRequests=super.friendRequestService.findAll()
                .stream()
                .filter(request -> request.getToUser().equals(loggedUser.getId()))
                .map(request -> {
                    Optional<User> fromUser=super.userService.findOne(request.getFromUser());
                    return fromUser.map(user -> new FriendRequestDTO(request.getId(), user.getFirstName(),
                            user.getLastName(),
                            request.getStatus(), request.getDate())).orElse(null);
                })
                .collect(Collectors.toList()));
    }

    private void updateFriendRequestList(Long id,String newStatus){
        allFriendRequests.stream()
                .filter(request -> request.getId().equals(id))
                .findAny()
                .ifPresent(request -> request.setStatus(newStatus));
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

    @Override
    public List<User> getAllUsers() {
        List<User> all = super.getAllUsers();
        return all.stream()
                .filter(user -> (!user.equals(loggedUser)))
                .collect(Collectors.toList());
    }
}
