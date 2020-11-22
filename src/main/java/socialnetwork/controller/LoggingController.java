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
import socialnetwork.domain.User;
import socialnetwork.service.MasterService;

import java.io.IOException;

public class LoggingController {

    private MasterService service;
    private User loggedUser = null;

    public void setService(MasterService service){
        this.service=service;
    }

    @FXML
    Button buttonLogIn;
    @FXML
    PasswordField passwordFieldId;
    @FXML
    TextField textFieldFirstName;
    @FXML
    TextField textFieldLastName;
    @FXML
    Button buttonAddUser;

    public void handleButtonLogInClicked(ActionEvent actionEvent) {
        System.out.println("S-a apasat");
        System.out.println(textFieldFirstName.getText()+" "+textFieldLastName.getText()+" "+passwordFieldId.getText());
        try {
            loggedUser = this.service.logging(textFieldFirstName.getText(),
                    textFieldLastName.getText(), Long.parseLong(passwordFieldId.getText()));
            closeWindow();
            showFriendshipsWindow();
        }catch (NumberFormatException e){
            LoggingAlert.showErrorMessage(null,"Invalid Id");
            System.out.println("Wrong");
        }
        catch (Exception e){
            LoggingAlert.showErrorMessage(null,"Wrong credentials");
            System.out.println("Wrong credentials");
        }
    }

    public void handleButtonAddUserClicked(ActionEvent actionEvent) {
        System.out.println("S-a apasat acest buton");
        closeWindow();
        showCreateAccountWindow();
        //TODO
    }

    private void showCreateAccountWindow(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/createAccount.fxml"));
            Pane root = loader.load();

            Stage createAccountStage = new Stage();
            createAccountStage.setTitle("Create a new account");
            createAccountStage.setScene(new Scene(root));

            CreateAccountController createAccountController = loader.getController();
            createAccountController.setService(service);
            createAccountStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void showFriendshipsWindow(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/friendships.fxml"));
            Pane root = loader.load();

            Stage friendshipsStage = new Stage();
            friendshipsStage.setTitle("Friendships");
            friendshipsStage.setScene(new Scene(root));

            FriendshipsController friendshipsController = loader.getController();
            friendshipsController.initialize(service,loggedUser);
            friendshipsStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void closeWindow(){
        Stage stage = (Stage)buttonLogIn.getScene().getWindow();
        stage.close();
    }
}
