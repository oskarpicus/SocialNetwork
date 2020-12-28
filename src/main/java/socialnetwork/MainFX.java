package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import socialnetwork.controller.LoggingController;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.*;
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
        primaryStage.setTitle("Social Network");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/logging.fxml"));
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
        EventService eventService = new EventService(eventRepository);

        NotificationValidator notificationValidator = new NotificationValidator();
        PagingRepository<Long,Notification> notificationRepository = new NotificationDBRepository(notificationValidator,"social_network");
        NotificationService notificationService = new NotificationService(notificationRepository);

        return new MasterService(friendshipService,userService, friendRequestService,messageService, eventService, notificationService);
    }

}
