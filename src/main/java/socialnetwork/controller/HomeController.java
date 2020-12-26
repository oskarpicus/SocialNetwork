package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import socialnetwork.controller.pages.PageActions;
import socialnetwork.domain.User;

public class HomeController extends AbstractController{

    @FXML
    Label labelGreeting;
    @FXML
    Label labelSearch;
    @FXML
    Label labelFriends;
    @FXML
    Label labelFriendRequests;

    @Override
    public void initialize(PageActions pageActions) {
        super.initialize(pageActions);
        User loggedUser = pageActions.getLoggedUser();
        labelGreeting.setText("Hello "+loggedUser.getFirstName()+" "+loggedUser.getLastName()+"!");
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage)labelGreeting.getScene().getWindow();
        stage.close();
    }

    public void handleLabelSearch(MouseEvent mouseEvent) {
        System.out.println("S-a apasat label search");
        openWindow("search");
    }

    public void handleLabelFriends(MouseEvent mouseEvent) {
        openWindow("friends");
    }

    public void handleLabelFriendRequests(MouseEvent mouseEvent) {
        openWindow("friendRequests");
    }

    public void handleLabelMessages(MouseEvent mouseEvent) {
        openWindow("messages");
    }
}
