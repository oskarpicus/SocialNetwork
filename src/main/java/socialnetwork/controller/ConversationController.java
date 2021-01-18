package socialnetwork.controller;

import javafx.application.Platform;
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
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import socialnetwork.controller.pages.PageActions;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.MessageDTO;
import socialnetwork.utils.Constants;
import socialnetwork.utils.events.message.MessageEvent;
import socialnetwork.utils.events.message.MessageEventType;
import socialnetwork.utils.observer.Observer;

import java.util.Collections;
import java.util.List;

public class ConversationController extends AbstractController implements Observer<MessageEvent> {

    private final ObservableList<MessageDTO> model = FXCollections.observableArrayList();
    private User userToMessage;
    private int rightLimit = Constants.ITEMS_LIST_VIEW_PAGE;
    private int numberOfMessages;

    @FXML
    ListView<MessageDTO> listViewConversation;
    @FXML
    TextField textFieldMessage;
    @FXML
    Label labelInformation;


    public void initialize(PageActions pageActions, User userToMessage) {
        super.initialize(pageActions);
        this.userToMessage=userToMessage;
        numberOfMessages = pageActions.getMessages(userToMessage).size();
        rightLimit=numberOfMessages;
        pageActions.getService().addMessageObserver(this);
        labelInformation.setText("Below is a list of all your the messages with "+userToMessage.getFirstName()+" "+userToMessage.getLastName());
        setData();
    }

    private void setData(){
        int nr = rightLimit-Constants.ITEMS_LIST_VIEW_PAGE;
        if(nr<0) nr = 0;
        model.setAll(pageActions.getMessages(nr,rightLimit,userToMessage));
        listViewConversation.setItems(model);
    }

    @Override
    public void update(MessageEvent event) {
        Platform.runLater(()->{
        if(event.getType()==MessageEventType.SEND || event.getType()==MessageEventType.REPLY) {
            rightLimit=(++numberOfMessages);
        }
        setData();});
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
            handleButtonSendMessage(null);
        }
    }

    public void handleButtonSendMessage(ActionEvent actionEvent) {
        if(textFieldMessage.getText().equals("")){
            MyAllert.showMessage(null, Alert.AlertType.WARNING,"Warning","Empty message");
            return;
        }
        MessageDTO message = getSelectedMessage();
        String text = textFieldMessage.getText();
        if(message==null){
            List<Long> list = Collections.singletonList(userToMessage.getId());
            pageActions.sendMessage(new Message(pageActions.getLoggedUser().getId(),list,text));
        }
        else{
            pageActions.replyMessage(message,text);
        }
        textFieldMessage.setText("");
    }

    public void handleScroll(ScrollEvent scrollEvent) {
        if(scrollEvent.getDeltaY()>0 && (rightLimit-Constants.ITEMS_LIST_VIEW_PAGE)>0){ //scroll up
            rightLimit--;
        }
        if(scrollEvent.getDeltaY()<0 && rightLimit!=numberOfMessages){ //scroll down and there are messages left
            rightLimit++;
        }
        setData();
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.UP) && (rightLimit-Constants.ITEMS_LIST_VIEW_PAGE)>0){
            rightLimit--;
        }
        if(keyEvent.getCode().equals(KeyCode.DOWN) && rightLimit!=numberOfMessages ){
            rightLimit++;
        }
        setData();
    }
}
