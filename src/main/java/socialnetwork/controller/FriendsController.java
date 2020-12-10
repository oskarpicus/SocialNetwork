package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.FriendshipDTO;
import socialnetwork.service.MasterService;
import socialnetwork.utils.events.friendship.FriendshipEvent;
import socialnetwork.utils.observer.Observer;
import socialnetwork.utils.runners.RemoveFriendRunner;


public class FriendsController extends AbstractController implements Observer<FriendshipEvent> {

    private final ObservableList<FriendshipDTO> model = FXCollections.observableArrayList();

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
        setTableViewData();
    }

    private void setTableViewData(){
        this.model.setAll(this.service.filterFriendshipsID(loggedUser.getId()));
        tableViewFriends.setItems(model);
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage) tableViewFriends.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update(FriendshipEvent event) {
        setTableViewData();
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
