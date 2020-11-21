package socialnetwork.service;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.User;

import java.util.Optional;

public class MasterServiceWithLogging extends MasterService {

    private User loggedUser;

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
        return super.acceptFriendRequest(id);
    }

    @Override
    public Optional<FriendRequest> rejectFriendRequest(Long id) {
        checkFriendRequestIsForLoggedUser(id);
        return super.rejectFriendRequest(id);
    }
}
