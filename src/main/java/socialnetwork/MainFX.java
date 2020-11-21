package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import socialnetwork.controller.LoggingController;
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
        Pane pane = loader.load();
        primaryStage.setScene(new Scene(pane));
        LoggingController loggingController = loader.getController();
        loggingController.setService(service);

    }

    private MasterService getMasterService(){
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

        return new MasterServiceWithLogging(friendshipService,userService, friendRequestService,messageService);
    }

}
