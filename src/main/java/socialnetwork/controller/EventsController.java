package socialnetwork.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import socialnetwork.controller.pages.PageActions;
import socialnetwork.domain.Event;
import socialnetwork.service.PagingService;
import socialnetwork.utils.events.event.EventEvent;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class EventsController extends AbstractController implements Observer<EventEvent>{

    private final ObservableList<Event> model = FXCollections.observableArrayList();

    @FXML
    TableView<Event> tableViewEvents;
    @FXML
    TableColumn<String,String> tableColumnName;
    @FXML
    TableColumn<LocalDateTime,String> tableColumnDate;
    @FXML
    TableColumn<String,String> tableColumnLocation;
    @FXML
    Button buttonParticipate;
    @FXML
    Button buttonUnsubscribe;
    @FXML
    Button buttonAddEvent;
    @FXML
    Pagination pagination;

    @Override
    public void initialize(PageActions pageActions) {
        super.initialize(pageActions);
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tableViewEvents.setItems(model);

        tableViewEvents.setRowFactory(tableView -> new TableRow<>(){
            private final Tooltip tooltip = new Tooltip();

            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if(item==null)
                    setTooltip(null);
                else {
                    tooltip.setText(item.getDescription());
                    setTooltip(tooltip);
                }
            }
        });

        buttonUnsubscribe.setDisable(true); //TODO to delete this line of code

        Platform.runLater(this::setPageCount);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                List<Event> list = pageActions.getEvents(param);
                model.setAll(list);
                tableViewEvents.setItems(model);
                if(list.isEmpty())
                    return null;
                return tableViewEvents;
            }
        });
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage)pagination.getScene().getWindow();
        stage.close();
    }

    public void handleLabelHome(MouseEvent mouseEvent) {
        openWindow("home");
    }

    public void handleLabelSearch(MouseEvent mouseEvent) {
        openWindow("search");
    }

    public void handleLabelFriends(MouseEvent mouseEvent) {
        openWindow("friends");
    }

    public void handleLabelFriendRequests(MouseEvent mouseEvent) {
        openWindow("friendRequests");
    }

    public void handleLabelMessages(MouseEvent mouseEvent) {
        openWindow("messages");
    }

    public void setPageCount(){
        pagination.setPageCount((int)Math.ceil((double)pageActions.getEvents().size()/ PagingService.pageSize));
    }

    @Override
    public void update(EventEvent event) {
        //TODO
    }

    public void handleTableViewClicked(MouseEvent mouseEvent) {
        Event event = getSelectedEvent();
        if(event!=null){
            if(event.getDate().isBefore(LocalDateTime.now())){
                buttonParticipate.setDisable(true);
                buttonUnsubscribe.setDisable(true);
                return;
            }
            buttonParticipate.setDisable(false);
            buttonUnsubscribe.setDisable(false);
            if(pageActions.isParticipant(event.getId()))
                buttonParticipate.setText("Can't go anymore");
            else
                buttonParticipate.setText("Participate");
        }
    }

    private Event getSelectedEvent(){
        return tableViewEvents.getSelectionModel().getSelectedItem();
    }

    public void handleButtonAddEvent(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/addEvent.fxml"));
            Pane pane = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle("Add Event");

            Controller controller = loader.getController();
            controller.initialize(pageActions);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handleButtonParticipate(ActionEvent actionEvent) {
    }

    public void handleButtonUnsubscribe(ActionEvent actionEvent) {
    }
}
