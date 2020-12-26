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
import socialnetwork.controller.pages.PageActions;
import socialnetwork.domain.dtos.FriendshipDTO;
import socialnetwork.service.PagingService;
import socialnetwork.utils.events.friendship.FriendshipEvent;
import socialnetwork.utils.observer.Observer;

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
    public void initialize(PageActions pageActions) {
        super.initialize(pageActions);
        pageActions.getService().addFriendshipObserver(this);
        tableColumnFriendsFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnFriendsLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnFriendsDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));

        Platform.runLater(this::setPageCount);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                var result = pageActions.getFriendships(param);
                model.setAll(result);
                tableViewFriends.setItems(model);
                if(result.isEmpty()){
                    return null;
                }
                return tableViewFriends;
            }
        });

    }

    public void setPageCount(){
        pagination.setPageCount((int)Math.ceil((double) pageActions.getService().filterFriendshipsID(pageActions.getLoggedUser().getId()).size()/PagingService.pageSize));
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage) tableViewFriends.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update(FriendshipEvent event) {
        List<FriendshipDTO> list = pageActions.getFriendships(pagination.getCurrentPageIndex());
        this.model.setAll(list);
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
        pageActions.removeFriendships(friendshipDTO);
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
