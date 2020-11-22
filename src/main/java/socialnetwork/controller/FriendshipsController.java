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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.UserDTO;
import socialnetwork.service.MasterServiceWithLogging;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;

public class FriendshipsController implements Observer {

    private MasterServiceWithLogging service;
    private User loggedUser;
    private final ObservableList<UserDTO> model = FXCollections.observableArrayList();

    @FXML
    Label labelGreeting;
    @FXML
    Button buttonRemoveFriend;
    @FXML
    Button buttonSendFriendRequest;
    @FXML
    Button buttonYourFriendRequests;
    @FXML
    TextField textFieldName;
    @FXML
    TableColumn<UserDTO,String> tableColumnFirstName;
    @FXML
    TableColumn<UserDTO,String> tableColumnLastName;
    @FXML
    TableColumn<UserDTO,String> tableColumnFriends;
    @FXML
    TableView<UserDTO> tableViewFriendships;

    public void initialize(MasterServiceWithLogging service,User loggedUser){
        this.service=service;
        this.loggedUser=loggedUser;
        labelGreeting.setText("Hello "+loggedUser.getFirstName()+" "+loggedUser.getLastName()+" !");
        service.addObserver(this);
        initTable();
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
        tableColumnFriends.setCellValueFactory(new PropertyValueFactory<>("friendsWithLoggedUser"));
        setTableViewData();
    }

    public void handleRemoveFriend(ActionEvent actionEvent) {
        UserDTO selected = getSelectedUser();
        Tuple<Long,Long> ids = selected.getId() < loggedUser.getId() ?
                new Tuple<>(selected.getId(),loggedUser.getId()) :
                new Tuple<>(loggedUser.getId(),selected.getId());
        if(this.service.removeFriendship(ids).isEmpty())
            MyAllert.showErrorMessage(null,"You are not friends with "+selected.getFirstName()+" "+selected.getLastName());
        else
            MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success","You are no longer friends");
    }

    public void handleSendFriendRequest(ActionEvent actionEvent) {
        UserDTO selected = getSelectedUser();
        try {
            if (this.service.sendFriendRequest(loggedUser.getId(), selected.getId()).isPresent())
                MyAllert.showErrorMessage(null,"The friend request could not be sent");
            else
                MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success","The friend request was sent successfully");
        }catch (Exception e){
            MyAllert.showErrorMessage(null,e.getMessage());
        }
    }

    public void handleYourFriendRequests(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/friendRequests.fxml"));
            Pane pane = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(pane));

            FriendRequestsController controller = loader.getController();
            controller.setService(service);

            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
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


}
