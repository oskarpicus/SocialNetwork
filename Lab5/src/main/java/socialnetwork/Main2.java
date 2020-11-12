package socialnetwork;

import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendRequestValidator;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.FriendRequestFile;
import socialnetwork.repository.file.FriendshipFile;
import socialnetwork.repository.file.UserFile;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MasterService;
import socialnetwork.service.UserService;
import socialnetwork.ui.ConsoleUI;

public class Main2 {

    public static void main(String[] args) {
        String fileNameUsers= ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
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
        MasterService masterService = new MasterService(friendshipService,userService, friendRequestService);

        ConsoleUI ui = new ConsoleUI(masterService);
        ui.run();
    }
}
