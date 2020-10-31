package socialnetwork;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.FriendshipDBRepository;
import socialnetwork.repository.database.UserDBRepository;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MasterService;
import socialnetwork.service.UserService;
import socialnetwork.ui.ConsoleUI;


public class Main3 {

    public static void main(String[] args) {
        Repository<Long, User> userDBRepository = new UserDBRepository(new UserValidator(),"social_network");
        Repository<Tuple<Long,Long>, Friendship> friendshipDBRepository = new FriendshipDBRepository(new FriendshipValidator(),"social_network");
        UserService userService = new UserService(userDBRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipDBRepository,userDBRepository);
        MasterService masterService = new MasterService(friendshipService,userService);
        ConsoleUI ui = new ConsoleUI(masterService);
        ui.run();
    }
}
