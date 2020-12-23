package socialnetwork;

import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.FriendRequestValidator;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.MessageDBRepository;
import socialnetwork.repository.file.FriendRequestFile;
import socialnetwork.repository.file.FriendshipFile;
import socialnetwork.repository.file.UserFile;
import socialnetwork.service.*;
import socialnetwork.ui.ConsoleUI;

public class MainCMDTextFile {

    public static void main(String[] args) {
       /* String fileNameUsers= ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        Repository<Long, User> userFileRepository = new UserFile(fileNameUsers
                , new UserValidator());
        UserService userService = new UserService(userFileRepository);

        String fileNameFriendship = "data/friendships.csv";
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository= new FriendshipFile(fileNameFriendship,
                friendshipValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipFileRepository, userFileRepository);

        FriendRequestValidator friendRequestValidator = new FriendRequestValidator();
        Repository<Long, FriendRequest> friendRequestRepository = new FriendRequestFile("data/friendRequests.csv",friendRequestValidator);
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository);

        MessageValidator messageValidator = new MessageValidator();
        Repository<Long, Message> messageRepository = new MessageDBRepository(messageValidator,"social_network");
        MessageService messageService = new MessageService(messageRepository);

        MasterService masterService = new MasterService(friendshipService,userService, friendRequestService,messageService);

        ConsoleUI ui = new ConsoleUI(masterService);
        ui.run();*/
    }
}
