package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.UserDTO;
import socialnetwork.service.MasterServiceWithLogging;
import socialnetwork.utils.observer.Observer;
import socialnetwork.utils.runners.RemoveFriendRunner;
import socialnetwork.utils.runners.SendFriendRequestRunner;

import java.io.IOException;

public class SearchController extends AbstractController implements Observer {

    private final ObservableList<UserDTO> model = FXCollections.observableArrayList();

    @FXML
    Button buttonSendFriendRequest;
    @FXML
    TextField textFieldName;
    @FXML
    TableColumn<UserDTO,String> tableColumnFirstName;
    @FXML
    TableColumn<UserDTO,String> tableColumnLastName;
    @FXML
    TableView<UserDTO> tableViewFriendships;
    @FXML
    Label labelHome;
    @FXML
    Label labelFriends;

    @Override
    public void initialize(MasterServiceWithLogging service,User loggedUser){
        super.initialize(service,loggedUser);
        service.addObserver(this);
        initTable();
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage) textFieldName.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update(){
        setTableViewData();
    }

    private void setTableViewData(){
        model.setAll(this.service.getAllUserDTO(loggedUser.getId()));
        tableViewFriendships.setItems(model);
    }

    private void initTable(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        setTableViewData();
    }

    public void handleSendFriendRequest(ActionEvent actionEvent) {
        UserDTO selected = getSelectedUser();
        if(selected==null)
            return;
        SendFriendRequestRunner runner = new SendFriendRequestRunner(loggedUser.getId(),selected.getId(),service);
        runner.execute();
    }

    public void handleYourFriendRequests(ActionEvent actionEvent) {
//        try{
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("/views/friendRequests.fxml"));
//            Pane pane = loader.load();
//            Stage stage = new Stage();
//            stage.setScene(new Scene(pane));
//
//            FriendRequestsController controller = loader.getController();
//            controller.setService(service);
//
//            stage.show();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
        openWindow("friendRequests");
    }

    public void handleTextFieldNameKeyTyped(KeyEvent keyEvent) {
        if(textFieldName.getText().equals(""))
            setTableViewData();
        model.setAll(this.service.filterUsers(textFieldName.getText()));
    }

    private UserDTO getSelectedUser(){
        UserDTO userDTO = tableViewFriendships.getSelectionModel().getSelectedItem();
        if(userDTO==null)
            MyAllert.showMessage(null, Alert.AlertType.WARNING,"Attention","You did not select a user");
        return userDTO;
    }

    public void handleLabelHome(MouseEvent mouseEvent) {
        openWindow("home");
    }

    public void handleLabelFriends(MouseEvent mouseEvent) {
        openWindow("friendRequests");
    }
}
