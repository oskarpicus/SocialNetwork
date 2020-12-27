package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import socialnetwork.controller.LoggingController;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.*;
import socialnetwork.repository.paging.PagingRepository;
import socialnetwork.service.*;

import java.io.IOException;

public class MainFX extends Application {

    private MasterService service;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("ok");
        service = getMasterService();
        initView(primaryStage);
        primaryStage.show();
  }

    public static void main(String[] args) {
        launch(args);
    }

    private void initView(Stage primaryStage) throws IOException{
       /* Parent root = FXMLLoader.load(getClass().getResource("/views/logging.fxml"));
        primaryStage.setScene(new Scene(root,300,300));*/
        primaryStage.setTitle("Social Network");
     /*   FXMLLoader loader = new FXMLLoader();
        Pane p = loader.load(getClass().getResource("/views/logging.fxml"));
        LoggingController controller = loader.getController();
        controller.setService(service);
        primaryStage.setScene(new Scene(p));*/


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/logging.fxml"));
       // loader.setLocation(getClass().getResource("/views/home.fxml"));
        Pane pane = loader.load();
        primaryStage.setScene(new Scene(pane));
        LoggingController loggingController = loader.getController();
        loggingController.setService(service);
    }

    private MasterService getMasterService(){
        PagingRepository<Long, User> userDBRepository = new UserDBRepository(new UserValidator(),"social_network");
        PagingRepository<Tuple<Long,Long>, Friendship> friendshipDBRepository = new FriendshipDBRepository(new FriendshipValidator(),"social_network");
        UserService userService = new UserService(userDBRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipDBRepository,userDBRepository);

        FriendRequestValidator friendRequestValidator = new FriendRequestValidator();
        PagingRepository<Long, FriendRequest> friendRequestRepository = new FriendRequestDBRepository(friendRequestValidator,"social_network");
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository);

        MessageValidator messageValidator = new MessageValidator();
        PagingRepository<Long, Message> messageRepository = new MessageDBRepository(messageValidator,"social_network");
        MessageService messageService = new MessageService(messageRepository);

        EventValidator eventValidator = new EventValidator();
        PagingRepository<Long,Event> eventRepository = new EventDBRepository(eventValidator,"social_network");

        return new MasterService(friendshipService,userService, friendRequestService,messageService);
    }

}
