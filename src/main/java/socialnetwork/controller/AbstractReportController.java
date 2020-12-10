package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.MessageDTO;
import socialnetwork.service.MasterService;

import java.io.File;

public abstract class AbstractReportController extends AbstractController {
    protected final ObservableList<MessageDTO> modelMessages = FXCollections.observableArrayList();
    protected final ToggleGroup group = new ToggleGroup();

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

    @Override
    public void initialize(MasterService service, User loggedUser) {
        super.initialize(service, loggedUser);
        radioButtonNo.setToggleGroup(group);
        radioButtonYes.setToggleGroup(group);
        radioButtonNo.setSelected(true);
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage) labelInformation.getScene().getWindow();
        stage.close();
    }

    protected String getPathToSave(){
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
