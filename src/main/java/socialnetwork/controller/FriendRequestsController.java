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
import socialnetwork.controller.pages.PageActions;
import socialnetwork.domain.dtos.FriendRequestDTO;
import socialnetwork.service.PagingService;
import socialnetwork.utils.Constants;
import socialnetwork.utils.events.friendRequest.FriendRequestEvent;
import socialnetwork.utils.observer.Observer;

import java.util.List;

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
        modelSentFriendRequests.setAll(pageActions.getSentFriendRequests(pageNumberSent));
        modelReceivedFriendRequests.setAll(pageActions.getReceivedFriendRequests(pageNumberReceived));
        Platform.runLater(this::setPageCount);
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage)labelFriends.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(PageActions pageActions) {
        super.initialize(pageActions);
        pageActions.getService().addFriendRequestObserver(this);
        initializeTableViewReceivedFriendRequests();
        initializeTableViewSentFriendRequests();
        Platform.runLater(this::setPageCount);
        tableViewReceivedFriendRequests.setFixedCellSize(Constants.SMALL_TABLE_VIEW_CELL_SIZE);
        tableViewSentFriendRequests.setFixedCellSize(Constants.SMALL_TABLE_VIEW_CELL_SIZE);
        paginationSentFriendRequests.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                List<FriendRequestDTO> result = pageActions.getSentFriendRequests(param);
                modelSentFriendRequests.setAll(result);
                tableViewSentFriendRequests.setItems(modelSentFriendRequests);
                if(result.isEmpty()){
                    return null;
                }
                return tableViewSentFriendRequests;
            }
        });
        paginationReceivedFriendRequests.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                List<FriendRequestDTO> result = pageActions.getReceivedFriendRequests(param);
                modelReceivedFriendRequests.setAll(result);
                tableViewReceivedFriendRequests.setItems(modelReceivedFriendRequests);
                if(result.isEmpty()){
                    return null;
                }
                return tableViewReceivedFriendRequests;
            }
        });
    }

    public void setPageCount(){
        paginationSentFriendRequests.setPageCount((int) Math.ceil((double)pageActions.getSentFriendRequests().size()/ PagingService.pageSize));
        paginationReceivedFriendRequests.setPageCount((int)Math.ceil((double)pageActions.getReceivedFriendRequests().size()/PagingService.pageSize));
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
        pageActions.acceptFriendRequest(request);
    }

    public void handleButtonRejectFriendRequest(ActionEvent actionEvent) {
        FriendRequestDTO request = getSelectedReceivedRequest();
        if(request==null)
            return;
        pageActions.rejectFriendRequest(request);
    }

    public void handleButtonRemoveFriendRequest(ActionEvent actionEvent) {
        FriendRequestDTO request = getSelectedSentRequest();
        if(request==null)
            return;
        pageActions.removeFriendRequest(request);
    }

    public void handleLabelEvents(MouseEvent mouseEvent) {
        openWindow("events");
    }
}
