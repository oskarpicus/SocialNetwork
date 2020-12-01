package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.MasterServiceWithLogging;

import java.io.IOException;

public class HomeController extends AbstractController{

    @FXML
    Label labelGreeting;
    @FXML
    Label labelSearch;
    @FXML
    Label labelFriends;

    @Override
    public void initialize(MasterServiceWithLogging service, User loggedUser) {
        super.initialize(service, loggedUser);
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
        openWindow("friendRequests");
    }
}
