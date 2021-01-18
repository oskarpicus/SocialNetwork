package socialnetwork.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import socialnetwork.controller.pages.PageActions;
import socialnetwork.controller.pages.PageObject;
import socialnetwork.domain.User;
import socialnetwork.service.MasterService;
import socialnetwork.utils.Converter;

import java.io.IOException;
import java.util.Optional;

public class LoggingController {

    private MasterService service;
    private User loggedUser = null;

    public void setService(MasterService service){
        this.service=service;
    }

    @FXML
    Button buttonLogIn;
    @FXML
    PasswordField passwordField;
    @FXML
    TextField textFieldUsername;
    @FXML
    Button buttonAddUser;

    public void handleButtonLogInClicked(ActionEvent actionEvent) {
        try {
            Optional<User> result = this.service.findUserByUserName(textFieldUsername.getText());

            if(result.isEmpty())
                throw new Exception();
            String password = Converter.hashPassword(passwordField.getText());
            if(!result.get().getPassword().equals(password))
                throw new Exception();
            loggedUser=result.get();
            showHomeWindow();
        }catch (NumberFormatException e){
            MyAllert.showErrorMessage(null,"Invalid Id");
            System.out.println("Wrong");
        }
        catch (Exception e){
            MyAllert.showErrorMessage(null,"Wrong credentials");
            System.out.println("Wrong credentials");
        }
    }

    public void handleButtonAddUserClicked(ActionEvent actionEvent) {
        showCreateAccountWindow();
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

    private void showHomeWindow(){
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

}
