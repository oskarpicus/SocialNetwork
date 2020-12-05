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
import socialnetwork.domain.Entity;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.service.MasterService;
import socialnetwork.utils.observer.Observer;
import socialnetwork.utils.runners.SendMessageRunner;

import java.util.List;
import java.util.stream.Collectors;

public class MessagesController extends AbstractController implements Observer {

    private final ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableViewUsers;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    @FXML
    TableColumn<User,String> tableColumnLastName;
    @FXML
    TextArea textAreaMessage;
    @FXML
    TextField textFieldSearchUser;

    @Override
    public void closeWindow() {
        Stage stage = (Stage)tableViewUsers.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(MasterService service, User loggedUser) {
        super.initialize(service, loggedUser);
        service.addObserver(this);
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableViewUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // it allows for selecting multiple rows at once with CTRL+Click
        setTableViewData();
    }

    private void setTableViewData(){
        model.setAll(this.getAllUsers(this.service.getAllUsers()));
        tableViewUsers.setItems(model);
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

    public void handleTextField(KeyEvent keyEvent) {
        if(textFieldSearchUser.getText().equals(""))
            setTableViewData();
        else {
            model.setAll(this.getAllUsers(this.service.filterUsers(textFieldSearchUser.getText())));
        }
    }

    @Override
    public void update() {
        setTableViewData();
    }

    private List<User> getAllUsers(List<User> listOfUsers){
        return listOfUsers
                .stream()
                .filter(user -> (!user.equals(loggedUser)))
                .collect(Collectors.toList());
    }

    private List<Long> getSelectedUsersIds(){
        List<User> all= tableViewUsers.getSelectionModel().getSelectedItems();
        if(all==null || all.isEmpty()) {
            MyAllert.showMessage(null, Alert.AlertType.WARNING, "Warning", "You did not select any user");
            return null;
        }
        return all.stream()
                .map(Entity::getId)
                .collect(Collectors.toList());
    }


    public void handleButtonSendMessage(ActionEvent actionEvent) {
        if(textAreaMessage.getText().equals("")){
            MyAllert.showMessage(null, Alert.AlertType.WARNING,"Warning","Your message caný be empty");
            return;
        }
        List<Long> selected = getSelectedUsersIds();
        if(selected==null || selected.isEmpty())
            return;
        Message message = new Message(loggedUser.getId(),selected,textAreaMessage.getText());
        SendMessageRunner runner = new SendMessageRunner(message,service);
        runner.execute();
    }

    public void handleTableViewClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount()==2){
            System.out.println(getSelectedUsersIds());
        }
    }
}
