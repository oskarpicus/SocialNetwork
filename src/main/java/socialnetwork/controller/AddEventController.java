package socialnetwork.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import socialnetwork.controller.pages.PageActions;
import socialnetwork.domain.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class AddEventController extends AbstractController {

    @FXML
    Spinner<LocalTime> spinner;
    @FXML
    TextField textFieldName;
    @FXML
    DatePicker datePicker;
    @FXML
    TextField textFieldLocation;
    @FXML
    TextArea textAreaDescription;

    @Override
    public void closeWindow() {
        Stage stage = (Stage)textAreaDescription.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(PageActions pageActions) {
        super.initialize(pageActions);
        spinner.setValueFactory(new SpinnerValueFactory<>() {
            @Override
            public void decrement(int steps) {
                if (getValue() == null)
                    setValue(LocalTime.now().truncatedTo(ChronoUnit.MINUTES));
                else
                    setValue(getValue().minusMinutes(steps));
            }

            @Override
            public void increment(int steps) {
                if (getValue() == null)
                    setValue(LocalTime.now().truncatedTo(ChronoUnit.MINUTES));
                else
                    setValue(getValue().plusMinutes(steps));
            }
        });
        datePicker.setDayCellFactory(picker -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });
    }

    public void handleButtonAddEvent(ActionEvent actionEvent) {
        String name = textFieldName.getText();
        String location = textFieldName.getText();
        String description = textAreaDescription.getText();
        LocalDate date = datePicker.getValue();
        LocalTime time = spinner.getValue();

        if(date==null || time==null){
            MyAllert.showMessage(null, Alert.AlertType.WARNING,"Warning","You did not select a date");
            return;
        }

        if(name.equals("") || location.equals("") || description.equals("")){
            MyAllert.showMessage(null, Alert.AlertType.WARNING,"Warning","You did not complete all fields");
            return;
        }

        LocalDateTime dateEvent = date.atTime(time);
        Event event = new Event(name,dateEvent,description,location);
        if(pageActions.addEvent(event).isEmpty()) {
            MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION, "Success", "Event added successfully");
            closeWindow();
        }
        else{
            MyAllert.showErrorMessage(null,"Failed to add event");
        }
    }
}
