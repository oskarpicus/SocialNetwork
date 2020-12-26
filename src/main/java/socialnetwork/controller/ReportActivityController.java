package socialnetwork.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import socialnetwork.controller.pages.PageActions;
import socialnetwork.domain.dtos.FriendshipDTO;
import socialnetwork.domain.dtos.MessageDTO;
import socialnetwork.utils.pdf.PdfGenerator;

import java.time.LocalDate;
import java.util.List;

public class ReportActivityController extends ReportMessagesController{

    private final ObservableList<FriendshipDTO> modelFriendships = FXCollections.observableArrayList();

    @FXML
    ListView<MessageDTO> listViewMessages;
    @FXML
    ListView<FriendshipDTO> listViewFriendships;

    @Override
    public void initialize(PageActions pageActions) {
        super.initialize(pageActions);
        listViewFriendships.setItems(modelFriendships);
        listViewMessages.setItems(modelMessages);
        labelInformation.setText("Report on "+ pageActions.getLoggedUser().getFirstName()+" "+ pageActions.getLoggedUser().getLastName()+"'s activity");
        listViewFriendships.setPlaceholder(new Label("This is where your\nfriendships will be displayed"));
        listViewMessages.setPlaceholder(new Label("This is where your\nmessages will show up"));
    }

    private void setMessageData(List<MessageDTO> list){
        Platform.runLater(()->{
            modelMessages.setAll(list);
            listViewMessages.setItems(modelMessages);});
    }

    private void setFriendshipData(List<FriendshipDTO> list){
        Platform.runLater(()->{
            modelFriendships.setAll(list);
            listViewFriendships.setItems(modelFriendships);
        });
    }

    public void handleButtonShowReportClicked(ActionEvent actionEvent) {
        LocalDate dateFrom = this.datePickerFrom.getValue();
        LocalDate dateTo = this.datePickerTo.getValue();
        if (dateFrom == null || dateTo == null) {
            MyAllert.showMessage(null, Alert.AlertType.WARNING, "Warning", "You did not select both days");
            return;
        }
        List<FriendshipDTO> friendships = pageActions.getFriendships(dateFrom,dateTo);
        setFriendshipData(friendships);
        if(friendships.isEmpty()){
            listViewFriendships.setPlaceholder(new Label("There are no friendships"));
        }
        List<MessageDTO> messages = pageActions.getMessages(dateFrom,dateTo);
        setMessageData(messages);
        if(messages.isEmpty()){
            listViewMessages.setPlaceholder(new Label("There are no messages"));
        }
        if(super.group.getSelectedToggle().equals(super.radioButtonYes)){
            String path = getPathToSave();
            if(path==null){
                MyAllert.showErrorMessage(null, "You did not select a location");
            }
            else{
                PdfGenerator.generateActivityReport(messages,friendships,path, pageActions.getLoggedUser(),dateFrom,dateTo);
                MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success","PDF saved successfully in "+path);
            }
        }
    }
}
