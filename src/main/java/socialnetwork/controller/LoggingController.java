package socialnetwork.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.MasterService;

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

    public void handleButtonLogInClicked(ActionEvent actionEvent) {
        System.out.println("S-a apasat");
        System.out.println(textFieldFirstName.getText()+" "+textFieldLastName.getText()+" "+passwordFieldId.getText());
        try {
            loggedUser = this.service.logging(textFieldFirstName.getText(),
                    textFieldLastName.getText(), Long.parseLong(passwordFieldId.getText()));
            Stage stage = (Stage)buttonLogIn.getScene().getWindow();
            stage.close();
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
        //TODO
    }
}
