package socialnetwork.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.FriendRequestDTO;
import socialnetwork.service.MasterService;
import socialnetwork.service.PagingService;
import socialnetwork.utils.events.friendRequest.FriendRequestEvent;
import socialnetwork.utils.observer.Observer;
import socialnetwork.utils.runners.AcceptFriendRequestRunner;
import socialnetwork.utils.runners.RejectFriendRequestRunner;
import socialnetwork.utils.runners.RemovePendingFriendRequestRunner;

import java.util.List;
import java.util.stream.Collectors;

public class FriendRequestsController extends AbstractController implements Observer<FriendRequestEvent> {

    private final ObservableList<FriendRequestDTO> modelSentFriendRequests = FXCollections.observableArrayList();
    private final ObservableList<FriendRequestDTO> modelReceivedFriendRequests = FXCollections.observableArrayList();

    @FXML
    Pagination paginationReceivedFriendRequests;
    @FXML
    Pagination paginationSentFriendRequests;
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
    public void update(FriendRequestEvent event) {
        int pageNumberSent = paginationSentFriendRequests.getCurrentPageIndex();
        int pageNumberReceived = paginationReceivedFriendRequests.getCurrentPageIndex();
        modelSentFriendRequests.setAll(service.getSentFriendRequestsPage(pageNumberSent,loggedUser));
        modelReceivedFriendRequests.setAll(service.getReceivedFriendRequestsPage(pageNumberReceived,loggedUser));
        Platform.runLater(this::setPageCount);
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage)labelFriends.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(MasterService service, User loggedUser) {
        super.initialize(service, loggedUser);
        service.addFriendRequestObserver(this);
        initializeTableViewReceivedFriendRequests();
        initializeTableViewSentFriendRequests();
        Platform.runLater(this::setPageCount);
        paginationSentFriendRequests.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                List<FriendRequestDTO> result = service.getSentFriendRequestsPage(param,loggedUser);
                modelSentFriendRequests.setAll(result);
                tableViewSentFriendRequests.setItems(modelSentFriendRequests);
                if(result.isEmpty()){
                   // if(service.getSentFriendRequestsPage(param-1,loggedUser).isEmpty())
                        return null;
                }
                return tableViewSentFriendRequests;
            }
        });
        paginationReceivedFriendRequests.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                List<FriendRequestDTO> result = service.getReceivedFriendRequestsPage(param,loggedUser);
                modelReceivedFriendRequests.setAll(result);
                tableViewReceivedFriendRequests.setItems(modelReceivedFriendRequests);
                if(result.isEmpty()){
                    //if(service.getSentFriendRequestsPage(param-1,loggedUser).isEmpty())
                        return null;
                }
                return tableViewReceivedFriendRequests;
            }
        });
    }

    public void setPageCount(){
        paginationSentFriendRequests.setPageCount((int) Math.ceil((double)getSentFriendRequests().size()/ PagingService.pageSize));
        paginationReceivedFriendRequests.setPageCount((int)Math.ceil((double)getReceivedFriendRequests().size()/PagingService.pageSize));
    }

    private void initializeTableViewSentFriendRequests(){
        tableColumnSentFirstName.setCellValueFactory(new PropertyValueFactory<>("toFirstName"));
        tableColumnSentLastName.setCellValueFactory(new PropertyValueFactory<>("toLastName"));
        tableColumnSentStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnSentDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));
        tableViewSentFriendRequests.setItems(modelSentFriendRequests);
    }

    private void initializeTableViewReceivedFriendRequests(){
        tableColumnReceivedFirstName.setCellValueFactory(new PropertyValueFactory<>("fromFirstName"));
        tableColumnReceivedLastName.setCellValueFactory(new PropertyValueFactory<>("fromLastName"));
        tableColumnReceivedStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnReceivedDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));
        tableViewReceivedFriendRequests.setItems(modelReceivedFriendRequests);
    }

    private List<FriendRequestDTO> getSentFriendRequests(){
        return this.service.getFriendRequestsDTO(this.service.getAllFriendRequests()).stream()
                .filter(friendRequestDTO -> friendRequestDTO.getUserFromId().equals(loggedUser.getId()))
                .collect(Collectors.toList());
    }

    private List<FriendRequestDTO> getReceivedFriendRequests(){
        return this.service.getFriendRequestsDTO(this.service.getAllFriendRequests()).stream()
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

    public void handleLabelMessages(MouseEvent mouseEvent) {
        openWindow("messages");
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
        FriendRequestDTO request = getSelectedSentRequest();
        if(request==null)
            return;
        RemovePendingFriendRequestRunner runner = new RemovePendingFriendRequestRunner(this.service,request.getId());
        runner.execute();
    }
}
