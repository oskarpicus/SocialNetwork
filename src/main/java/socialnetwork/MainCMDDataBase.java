package socialnetwork;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.FriendRequestValidator;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.FriendRequestDBRepository;
import socialnetwork.repository.database.FriendshipDBRepository;
import socialnetwork.repository.database.MessageDBRepository;
import socialnetwork.repository.database.UserDBRepository;
import socialnetwork.service.*;
import socialnetwork.ui.ConsoleUI;


public class MainCMDDataBase {

    public static void main(String[] args) {
        Repository<Long, User> userDBRepository = new UserDBRepository(new UserValidator(),"social_network");
        Repository<Tuple<Long,Long>, Friendship> friendshipDBRepository = new FriendshipDBRepository(new FriendshipValidator(),"social_network");
        UserService userService = new UserService(userDBRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipDBRepository,userDBRepository);

        FriendRequestValidator friendRequestValidator = new FriendRequestValidator();
        Repository<Long, FriendRequest> friendRequestRepository = new FriendRequestDBRepository(friendRequestValidator,"social_network");
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository);

        MessageValidator messageValidator = new MessageValidator();
        Repository<Long, Message> messageRepository = new MessageDBRepository(messageValidator,"social_network");
        MessageService messageService = new MessageService(messageRepository);

        MasterService masterService = new MasterService(friendshipService,userService, friendRequestService,messageService);
        ConsoleUI ui = new ConsoleUI(masterService);
        ui.run();
    }
}
