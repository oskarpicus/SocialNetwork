package socialnetwork.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import socialnetwork.controller.pages.PageActions;
import socialnetwork.controller.pages.PageObject;
import socialnetwork.domain.User;
import socialnetwork.service.MasterService;

import java.io.IOException;

public class CreateAccountController {

    private MasterService service;

    @FXML
    TextField textFieldFirstName;
    @FXML
    TextField textFieldLastName;
    @FXML
    TextField textFieldUserName;
    @FXML
    PasswordField textFieldPassword;
    @FXML
    PasswordField textFieldConfirmPassword;
    @FXML
    Button buttonSignUp;

    public void setService(MasterService service){
        this.service=service;
    }

    public void handleButtonSignUpClicked(ActionEvent actionEvent) {
        try{
            if(!textFieldPassword.getText().equals(textFieldConfirmPassword.getText()))
                throw new Exception("Passwords do not match");
            User user = new User(textFieldFirstName.getText(),
                    textFieldLastName.getText(),textFieldUserName.getText(),textFieldPassword.getText());
            if(this.service.addUser(user).isPresent())
                throw new Exception("You cannot use this username");
            MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION,"Welcome to the network","We hope you'll enjoy our community");
            this.closeWindow();
            showHomeWindow(user);
        } catch (Exception e) {
            MyAllert.showErrorMessage(null, e.getMessage());
        }
    }

    private void showHomeWindow(User loggedUser){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/home.fxml"));
            Pane root = loader.load();

            Stage homeStage = new Stage();
            homeStage.setTitle("Home");
            homeStage.setScene(new Scene(root));

            PageObject pageObject = new PageObject(service,loggedUser);
            PageActions pageActions = new PageActions(pageObject);

            HomeController homeController = loader.getController();
            homeController.initialize(pageActions);
            homeStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void closeWindow(){
        Stage stage = (Stage)buttonSignUp.getScene().getWindow();
        stage.close();
    }
}
