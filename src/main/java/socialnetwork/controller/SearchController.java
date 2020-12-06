package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.MasterService;
import socialnetwork.utils.events.user.UserEvent;
import socialnetwork.utils.observer.Observer;
import socialnetwork.utils.runners.SendFriendRequestRunner;

import java.util.List;
import java.util.stream.Collectors;

public class SearchController extends AbstractController implements Observer<UserEvent> {

    private final ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    Button buttonSendFriendRequest;
    @FXML
    TextField textFieldName;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    @FXML
    TableColumn<User,String> tableColumnLastName;
    @FXML
    TableView<User> tableViewUsers;
    @FXML
    Label labelHome;
    @FXML
    Label labelFriends;
    @FXML
    Label labelFriendRequests;

    @Override
    public void initialize(MasterService service, User loggedUser){
        super.initialize(service,loggedUser);
        service.addUserObserver(this);
        initTable();
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage) textFieldName.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update(UserEvent e){
        setTableViewData();
    }

    private void setTableViewData(){
        model.setAll(getAllUsers(this.service.getAllUsers()));
        tableViewUsers.setItems(model);
    }

    private List<User> getAllUsers(List<User> listOfUsers){
        return listOfUsers
                .stream()
                .filter(user -> (!user.equals(loggedUser)))
                .collect(Collectors.toList());
    }

    private void initTable(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        setTableViewData();
    }

    public void handleSendFriendRequest(ActionEvent actionEvent) {
        User selected = getSelectedUser();
        if(selected==null)
            return;
        SendFriendRequestRunner runner = new SendFriendRequestRunner(loggedUser.getId(),selected.getId(),service);
        runner.execute();
    }

    public void handleTextFieldNameKeyTyped(KeyEvent keyEvent) {
        if(textFieldName.getText().equals(""))
            setTableViewData();
        else {
            model.setAll(this.getAllUsers(this.service.filterUsers(textFieldName.getText())));
        }
    }

    private User getSelectedUser(){
        User user = tableViewUsers.getSelectionModel().getSelectedItem();
        if(user==null)
            MyAllert.showMessage(null, Alert.AlertType.WARNING,"Attention","You did not select a user");
        return user;
    }

    public void handleLabelHome(MouseEvent mouseEvent) {
        openWindow("home");
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
}
