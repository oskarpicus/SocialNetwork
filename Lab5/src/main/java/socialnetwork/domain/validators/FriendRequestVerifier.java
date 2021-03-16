package socialnetwork.domain.validators;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Tuple;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.UserService;
import java.util.function.Predicate;

//validates the entity, in relation with the other entities
public class FriendRequestVerifier {

    private final FriendshipService friendshipService;
    private final UserService userService;
    private final FriendRequestService friendRequestService;

    public FriendRequestVerifier(FriendshipService friendshipService, UserService userService, FriendRequestService friendRequestService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.friendRequestService = friendRequestService;
    }


    public void validate(Long fromId,Long toId) throws ValidationException {
        if(userNotExists(fromId))
            throw new ValidationException(fromId+" user does not exist");
        if(userNotExists(toId))
            throw new ValidationException(toId+" user does not exist");
        if(friendshipsExists(fromId,toId))
            throw new ValidationException(fromId+" and "+toId+" are already friends");
        if(alreadySent(fromId,toId))
            throw new ValidationException("The friend request was already sent");
    }

    private boolean userNotExists(Long id){
        //we verify that the IDs refer actual users
        return this.userService.findOne(id).isEmpty();
    }

    private boolean friendshipsExists(Long id1,Long id2){
        //we verify that there is no friendship between these users
        Tuple<Long,Long> ids = (id1 < id2) ? new Tuple<>(id1,id2) : new Tuple<>(id2,id1);
        return this.friendshipService.findOne(ids).isPresent();
    }

    private boolean alreadySent(Long fromId,Long toId){
        //we verify is the friend request was already sent (either in the same form, or inverse)
        Predicate<FriendRequest> predicateInverse = friendRequest -> friendRequest.getFromUser().equals(toId) &&
                friendRequest.getToUser().equals(fromId);
        Predicate<FriendRequest> predicate = predicateInverse.or(friendRequest ->
                friendRequest.getFromUser().equals(fromId) &&
                        friendRequest.getToUser().equals(toId) &&
                        friendRequest.getStatus().equals("pending")
        );
        return this.friendRequestService.findAll().stream()
                .anyMatch(predicate);
    }

}
