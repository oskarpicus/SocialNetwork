package socialnetwork.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.FriendshipDTO;
import socialnetwork.service.MasterService;
import socialnetwork.service.PagingService;
import socialnetwork.utils.events.friendship.FriendshipEvent;
import socialnetwork.utils.observer.Observer;
import socialnetwork.utils.runners.RemoveFriendRunner;

import java.util.List;


public class FriendsController extends AbstractController implements Observer<FriendshipEvent> {

    private final ObservableList<FriendshipDTO> model = FXCollections.observableArrayList();

    @FXML
    Pagination pagination;
    @FXML
    TableColumn<FriendshipDTO,String> tableColumnFriendsFirstName;
    @FXML
    TableColumn<FriendshipDTO,String> tableColumnFriendsLastName;
    @FXML
    TableColumn<FriendshipDTO, String> tableColumnFriendsDate;
    @FXML
    TableView<FriendshipDTO> tableViewFriends;

    @Override
    public void initialize(MasterService service, User loggedUser) {
        super.initialize(service, loggedUser);
        this.service.addFriendshipObserver(this);
        tableColumnFriendsFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnFriendsLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnFriendsDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));

        Platform.runLater(this::setPageCount);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                List<FriendshipDTO> result=service.getFriendshipsPage(param,loggedUser);
                model.setAll(result);
                tableViewFriends.setItems(model);
                if(result.isEmpty()){
                   // if(service.getFriendshipsPage(param-1,loggedUser).isEmpty()) //the previous
                        return null;
                }
                return tableViewFriends;
            }
        });

    }

    public void setPageCount(){
        pagination.setPageCount((int)Math.ceil((double)service.filterFriendshipsID(loggedUser.getId()).size()/PagingService.pageSize));
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage) tableViewFriends.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update(FriendshipEvent event) {
        this.model.setAll(service.getFriendshipsPage(pagination.getCurrentPageIndex(),loggedUser));
        Platform.runLater(this::setPageCount);
    }

    public void handleLabelHome(MouseEvent mouseEvent) {
        openWindow("home");
    }

    public void handleLabelSearch(MouseEvent mouseEvent) {
        openWindow("search");
    }

    public void handleLabelFriendRequests(MouseEvent mouseEvent) {
        openWindow("friendRequests");
    }


    public void buttonRemoveFriendship(ActionEvent actionEvent) {
        FriendshipDTO friendshipDTO = getSelectedFriendship();
        if(friendshipDTO==null)
            return;
        RemoveFriendRunner runner = new RemoveFriendRunner(friendshipDTO.getIds(),this.service);
        runner.execute();
    }

    private FriendshipDTO getSelectedFriendship(){
        FriendshipDTO friendshipDTO = this.tableViewFriends.getSelectionModel().getSelectedItem();
        if(friendshipDTO==null)
            MyAllert.showErrorMessage(null,"You did not select a friendship");
        return friendshipDTO;
    }

    public void handleLabelMessages(MouseEvent mouseEvent) {
        openWindow("messages");
    }
}
