package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import socialnetwork.domain.dtos.FriendRequestDTO;
import socialnetwork.service.MasterServiceWithLogging;
import socialnetwork.utils.observer.Observer;
import socialnetwork.utils.runners.AcceptFriendRequestRunner;
import socialnetwork.utils.runners.RejectFriendRequestRunner;

public class FriendRequestsController implements Observer {

    private final ObservableList<FriendRequestDTO> model = FXCollections.observableArrayList();
    private MasterServiceWithLogging service;

    @FXML
    TableView<FriendRequestDTO> tableViewFriendRequests;
    @FXML
    Button buttonAcceptFriendRequest;
    @FXML
    Button buttonRejectFriendRequest;
    @FXML
    TableColumn<FriendRequestDTO,String> tableColumnFirstName;
    @FXML
    TableColumn<FriendRequestDTO,String> tableColumnLastName;
    @FXML
    TableColumn<FriendRequestDTO,String> tableColumnStatus;
    @FXML
    TableColumn<FriendRequestDTO,String> tableColumnDate;

    @Override
    public void update() {
        setTableViewData();
    }

    public void setService(MasterServiceWithLogging service){
        this.service=service;
        service.addObserver(this);
        setTableViewData();
    }

    @FXML
    public void initialize(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("fromFirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("fromLastName"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));
    }

    private void setTableViewData(){
        model.setAll(this.service.getAllFriendRequestsDTO());
        tableViewFriendRequests.setItems(model);
    }

    public void handleButtonAcceptClicked(ActionEvent actionEvent) {
        FriendRequestDTO request = getSelectedRequest();
        if(request==null)
            return;
        Long id = request.getId();
        AcceptFriendRequestRunner runner = new AcceptFriendRequestRunner(id,service);
        runner.execute();
    }

    public void handleButtonRejectClicked(ActionEvent actionEvent) {
        FriendRequestDTO request = getSelectedRequest();
        if(request==null)
            return;
        Long id = request.getId();
        RejectFriendRequestRunner runner = new RejectFriendRequestRunner(id,service);
        runner.execute();
    }

    private FriendRequestDTO getSelectedRequest(){
        FriendRequestDTO request = tableViewFriendRequests.getSelectionModel().getSelectedItem();
        if(request==null)
            MyAllert.showMessage(null, Alert.AlertType.WARNING,"Attention","You did not select a friend request");
        return request;
    }
}
