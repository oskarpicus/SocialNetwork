package socialnetwork.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.MasterService;

import java.io.IOException;

public class CreateAccountController {

    private MasterService service;

    @FXML
    TextField textFieldFirstNameCreateAccount;
    @FXML
    TextField textFieldLastNameCreateAccount;
    @FXML
    Button buttonSignUp;

    public void setService(MasterService service){
        this.service=service;
    }

    public void handleButtonSignUpClicked(ActionEvent actionEvent) {
        User user = new User(textFieldFirstNameCreateAccount.getText(),
                textFieldLastNameCreateAccount.getText());
        try{
            this.service.addUser(user);
            LoggingAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Welcome to the network","Your ID is "+user.getId());
            this.closeWindow();
            showFriendshipsWindow(user);
        }catch (ValidationException e) {
            LoggingAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void showFriendshipsWindow(User loggedUser){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/friendships.fxml"));
            Pane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Friendships");
            stage.setScene(new Scene(root));

            FriendshipsController friendshipsController = loader.getController();
            friendshipsController.initialize(service,loggedUser);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void closeWindow(){
        Stage stage = (Stage)buttonSignUp.getScene().getWindow();
        stage.close();
    }
}
