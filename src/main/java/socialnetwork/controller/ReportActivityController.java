package socialnetwork.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.FriendshipDTO;
import socialnetwork.domain.dtos.MessageDTO;
import socialnetwork.service.MasterService;

import java.time.LocalDate;
import java.util.List;

public class ReportActivityController extends ReportMessagesController{

    private final ObservableList<FriendshipDTO> modelFriendships = FXCollections.observableArrayList();

    @FXML
    ListView<MessageDTO> listViewMessages;
    @FXML
    ListView<FriendshipDTO> listViewFriendships;

    @Override
    public void initialize(MasterService service, User loggedUser) {
        super.initialize(service, loggedUser);
        listViewFriendships.setItems(modelFriendships);
        listViewMessages.setItems(modelMessages);
        labelInformation.setText("Report on "+loggedUser.getFirstName()+" "+loggedUser.getLastName()+"'s activity");
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
        List<FriendshipDTO> friendships = service.filterFriendshipsIDDate(loggedUser.getId(),dateFrom,dateTo);
        setFriendshipData(friendships);
        List<MessageDTO> messages = service.getOnesMessages(loggedUser,dateFrom,dateTo);
        setMessageData(messages);
    }
}
