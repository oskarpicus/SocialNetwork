package socialnetwork.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.MessageDTO;
import socialnetwork.service.MasterService;
import socialnetwork.utils.pdf.PdfGenerator;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class ReportMessagesController extends AbstractController{

    private final ObservableList<MessageDTO> modelMessages = FXCollections.observableArrayList();
    private User userMessage = null;
    private final ToggleGroup group = new ToggleGroup();

    @FXML
    Label labelInformation;
    @FXML
    DatePicker datePickerFrom;
    @FXML
    DatePicker datePickerTo;
    @FXML
    RadioButton radioButtonYes;
    @FXML
    RadioButton radioButtonNo;
    @FXML
    ListView<MessageDTO> listViewMessages;



    @Override
    public void closeWindow() {
        Stage stage = (Stage) labelInformation.getScene().getWindow();
        stage.close();
    }

    //initialising for messages report
    public void initialize(MasterService service, User loggedUser, User userMessage){
        super.initialize(service,loggedUser);
        this.userMessage=userMessage;
        radioButtonNo.setToggleGroup(this.group);
        radioButtonYes.setToggleGroup(this.group);
        radioButtonNo.setSelected(true);
        listViewMessages.setItems(modelMessages);
        this.labelInformation.setText("Report containing your messages with \n"+userMessage.getFirstName()+" "+userMessage.getLastName());
        //addListViewFriendships();
    }

    private void setMessageData(List<MessageDTO> list){
        Platform.runLater(()->{
            modelMessages.setAll(list);
            listViewMessages.setItems(modelMessages);});
    }

    public void handleButtonShowReport(ActionEvent actionEvent) {
        if(userMessage!=null)
            reportMessage();
    }

    private void reportMessage(){
        LocalDate dateFrom = this.datePickerFrom.getValue();
        LocalDate dateTo = this.datePickerTo.getValue();
        if (dateFrom == null || dateTo == null) {
            MyAllert.showMessage(null, Alert.AlertType.WARNING, "Warning", "You did not select both days");
            return;
        }
        List<MessageDTO> result = this.service.getConversation(loggedUser, userMessage, dateFrom, dateTo);
        if (this.group.getSelectedToggle().equals(this.radioButtonYes)) {
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
            MyAllert.showMessage(null, Alert.AlertType.INFORMATION, "Information", "There are no messages");
        }
        setMessageData(result);
    }

    private String getPathToSave(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF","*.pdf"));
        File file = fileChooser.showSaveDialog(labelInformation.getScene().getWindow());
        if(file!=null){
            System.out.println(file);
            return file.getAbsolutePath();
        }
        return null;
    }

}
