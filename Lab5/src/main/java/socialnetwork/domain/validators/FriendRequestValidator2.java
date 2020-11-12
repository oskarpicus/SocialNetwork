package socialnetwork.domain.validators;

import socialnetwork.domain.FriendRequest;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.UserService;

//validates the entity, in relation with the other entities
public class FriendRequestValidator2  implements Validator<FriendRequest> {

    private FriendshipService friendshipService;
    private UserService userService;
    private FriendRequestService friendRequestService;

    public FriendRequestValidator2(FriendshipService friendshipService, UserService userService, FriendRequestService friendRequestService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.friendRequestService = friendRequestService;
    }


    @Override
    public void validate(FriendRequest entity) throws ValidationException {

    }

    private boolean userExists(Long id){
        return this.userService.findOne(id).isPresent();
    }
}
