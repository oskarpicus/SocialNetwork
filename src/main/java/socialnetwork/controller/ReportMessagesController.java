package socialnetwork.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import socialnetwork.controller.pages.PageActions;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.MessageDTO;
import socialnetwork.utils.pdf.PdfGenerator;

import java.time.LocalDate;
import java.util.List;

public class ReportMessagesController extends AbstractReportController{

    private User userMessage;

    @FXML
    ListView<MessageDTO> listViewMessages;

    public void initialize(PageActions pageActions, User userMessage){
        super.initialize(pageActions);
        this.userMessage=userMessage;
        this.labelInformation.setText("Report containing your messages with \n"+userMessage.getFirstName()+" "+userMessage.getLastName());
        listViewMessages.setPlaceholder(new Label("This is where your \nmessages will show up"));
    }

    private void setMessageData(List<MessageDTO> list){
        Platform.runLater(()->{
            modelMessages.setAll(list);
            listViewMessages.setItems(modelMessages);});
    }

    public void handleButtonShowReport(ActionEvent actionEvent) {
        LocalDate dateFrom = this.datePickerFrom.getValue();
        LocalDate dateTo = this.datePickerTo.getValue();
        if (dateFrom == null || dateTo == null) {
            MyAllert.showMessage(null, Alert.AlertType.WARNING, "Warning", "You did not select both days");
            return;
        }
        List<MessageDTO> result = pageActions.getMessages(userMessage,dateFrom,dateTo);
        if (super.group.getSelectedToggle().equals(this.radioButtonYes)) {
            String path = getPathToSave();
            if (path == null) {
                MyAllert.showErrorMessage(null, "You did not select a location");
            }
            else {
                PdfGenerator.generateMessagesReport(result, path, userMessage, dateFrom, dateTo);
                MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success","PDF saved successfully in "+path);
            }
        }
        if (result.isEmpty()) {
            listViewMessages.setPlaceholder(new Label("There are no messages"));
        }
        setMessageData(result);
    }
}
