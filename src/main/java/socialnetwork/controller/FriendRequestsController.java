package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.FriendRequestDTO;
import socialnetwork.service.MasterService;
import socialnetwork.utils.observer.Observer;
import socialnetwork.utils.runners.AcceptFriendRequestRunner;
import socialnetwork.utils.runners.RejectFriendRequestRunner;

import java.util.List;
import java.util.stream.Collectors;

public class FriendRequestsController extends AbstractController implements Observer {

    private final ObservableList<FriendRequestDTO> modelSentFriendRequests = FXCollections.observableArrayList();
    private final ObservableList<FriendRequestDTO> modelReceivedFriendRequests = FXCollections.observableArrayList();

    @FXML
    Label labelFriends;
    @FXML
    Label labelFriendRequests;
    @FXML
    TableView<FriendRequestDTO> tableViewSentFriendRequests;
    @FXML
    TableView<FriendRequestDTO> tableViewReceivedFriendRequests;
    @FXML
    TableColumn<FriendRequestDTO,String> tableColumnSentFirstName;
    @FXML
    TableColumn<FriendRequestDTO,String> tableColumnReceivedFirstName;
    @FXML
    TableColumn<FriendRequestDTO,String> tableColumnSentLastName;
    @FXML
    TableColumn<FriendRequestDTO,String> tableColumnReceivedLastName;
    @FXML
    TableColumn<FriendRequestDTO,String> tableColumnSentStatus;
    @FXML
    TableColumn<FriendRequestDTO,String> tableColumnReceivedStatus;
    @FXML
    TableColumn<FriendRequestDTO,String> tableColumnSentDate;
    @FXML
    TableColumn<FriendRequestDTO,String> tableColumnReceivedDate;
    @FXML
    Button buttonRemoveFriendRequest;
    @FXML
    Button buttonAcceptFriendRequest;
    @FXML
    Button buttonRejectFriendRequest;


    @Override
    public void update() {
        setTableViewData();
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage)labelFriends.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(MasterService service, User loggedUser) {
        super.initialize(service, loggedUser);
        service.addObserver(this);
        initializeTableViewReceivedFriendRequests();
        initializeTableViewSentFriendRequests();
        setTableViewData();
    }

    private void initializeTableViewSentFriendRequests(){
        tableColumnSentFirstName.setCellValueFactory(new PropertyValueFactory<>("toFirstName"));
        tableColumnSentLastName.setCellValueFactory(new PropertyValueFactory<>("toFirstName"));
        tableColumnSentStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnSentDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));
    }

    private void initializeTableViewReceivedFriendRequests(){
        tableColumnReceivedFirstName.setCellValueFactory(new PropertyValueFactory<>("fromFirstName"));
        tableColumnReceivedLastName.setCellValueFactory(new PropertyValueFactory<>("fromFirstName"));
        tableColumnReceivedStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnReceivedDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));
    }

    private void setTableViewData(){
        setSentTableViewData();
        setReceivedTableViewData();
    }

    private void setSentTableViewData(){
        modelSentFriendRequests.setAll(this.getSentFriendRequests());
        tableViewSentFriendRequests.setItems(modelSentFriendRequests);
    }

    private void setReceivedTableViewData(){
        modelReceivedFriendRequests.setAll(this.getReceivedFriendRequests());
        tableViewReceivedFriendRequests.setItems(modelReceivedFriendRequests);
    }

    private List<FriendRequestDTO> getSentFriendRequests(){
        return this.service.getAllFriendRequestsDTO().stream()
                .filter(friendRequestDTO -> friendRequestDTO.getUserFromId().equals(loggedUser.getId()))
                .collect(Collectors.toList());
    }

    private List<FriendRequestDTO> getReceivedFriendRequests(){
        return this.service.getAllFriendRequestsDTO().stream()
                .filter(friendRequestDTO -> friendRequestDTO.getUserToId().equals(loggedUser.getId()))
                .collect(Collectors.toList());
    }

    private FriendRequestDTO getSelectedSentRequest(){
        FriendRequestDTO request = tableViewSentFriendRequests.getSelectionModel().getSelectedItem();
        if(request==null)
            MyAllert.showMessage(null, Alert.AlertType.WARNING,"Attention","You did not select a friend request");
        return request;
    }

    private FriendRequestDTO getSelectedReceivedRequest(){
        FriendRequestDTO request = tableViewReceivedFriendRequests.getSelectionModel().getSelectedItem();
        if(request==null)
            MyAllert.showMessage(null, Alert.AlertType.WARNING,"Attention","You did not select a friend request");
        return request;
    }

    public void handleLabelFriends(MouseEvent mouseEvent) {
        openWindow("friends");
    }

    public void handleLabelSearch(MouseEvent mouseEvent) {
        openWindow("search");
    }

    public void handleLabelHome(MouseEvent mouseEvent) {
        openWindow("home");
    }

    public void handleButtonAcceptFriendRequest(ActionEvent actionEvent) {
        FriendRequestDTO request = getSelectedReceivedRequest();
        if(request==null)
            return;
        AcceptFriendRequestRunner runner = new AcceptFriendRequestRunner(request.getId(),this.service);
        runner.execute();
    }

    public void handleButtonRejectFriendRequest(ActionEvent actionEvent) {
        FriendRequestDTO request = getSelectedReceivedRequest();
        if(request==null)
            return;
        RejectFriendRequestRunner runner = new RejectFriendRequestRunner(request.getId(),this.service);
        runner.execute();
    }

    public void handleButtonRemoveFriendRequest(ActionEvent actionEvent) {
        //TODO implement it - also in service
    }
}
