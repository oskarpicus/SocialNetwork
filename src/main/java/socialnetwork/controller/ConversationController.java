package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.MessageDTO;
import socialnetwork.service.MasterService;
import socialnetwork.utils.observer.Observer;
import socialnetwork.utils.runners.ReplyMessageRunner;
import socialnetwork.utils.runners.Runner;
import socialnetwork.utils.runners.SendMessageRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConversationController extends AbstractController implements Observer {

    private final ObservableList<MessageDTO> model = FXCollections.observableArrayList();
    private User userToMessage;

    @FXML
    ListView<MessageDTO> listViewConversation;
    @FXML
    TextField textFieldMessage;
    @FXML
    Label labelInformation;


    public void initialize(MasterService service, User loggedUser,User userToMessage) {
        super.initialize(service, loggedUser);
        this.userToMessage=userToMessage;
        service.addObserver(this);
        labelInformation.setText("Below is a list of all your the messages with "+userToMessage.getFirstName()+" "+userToMessage.getLastName());
        setData();
    }

    private void setData(){
        model.setAll(this.service.getConversation(loggedUser.getId(),userToMessage.getId()));
        listViewConversation.setItems(model);
    }

    @Override
    public void update() {
        setData(); //todo problem - exception
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage)labelInformation.getScene().getWindow();
        stage.close();
    }

    private MessageDTO getSelectedMessage(){
        return listViewConversation.getSelectionModel().getSelectedItem();
    }

    public void handleTextFieldKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            System.out.println("a apasat enter"); //todo
            handleButtonSendMessage(null);

        }
    }

    public void handleButtonSendMessage(ActionEvent actionEvent) {
        //todo
        if(textFieldMessage.getText().equals("")){
            MyAllert.showMessage(null, Alert.AlertType.WARNING,"Warning","Empty message");
            return;
        }
        MessageDTO message = getSelectedMessage();
        String text = textFieldMessage.getText();
        Runner runner;
        if(message==null){
            List<Long> list = Collections.singletonList(userToMessage.getId());
            runner = new SendMessageRunner(new Message(loggedUser.getId(),list,text),this.service);
        }
        else{
            runner = new ReplyMessageRunner(message.getMessageId(),loggedUser.getId(),text,service);
        }
        runner.execute();
        textFieldMessage.setText("");
    }
}
