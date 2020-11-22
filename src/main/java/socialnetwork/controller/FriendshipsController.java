package socialnetwork.controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.UserDTO;
import socialnetwork.service.MasterService;

import java.util.List;

public class FriendshipsController {

    private MasterService service;
    private User loggedUser;
    private List<UserDTO> allUsers;
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

    public void initialize(MasterService service,User loggedUser){
        this.service=service;
        this.loggedUser=loggedUser;
        labelGreeting.setText("Hello "+loggedUser.getFirstName()+" "+loggedUser.getLastName()+" !");
        initTable();
    }

    private void initTable(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<UserDTO,String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<UserDTO,String>("lastName"));
        tableColumnFriends.setCellValueFactory(new PropertyValueFactory<UserDTO,String>("friendsWithLoggedUser"));
        allUsers = service.getAllUserDTO(loggedUser.getId());
        model.setAll(allUsers);
        tableViewFriendships.setItems(model);
    }

    public void handleRemoveFriend(ActionEvent actionEvent) {
        UserDTO selected = getSelectedUser();
        Tuple<Long,Long> ids = selected.getId() < loggedUser.getId() ?
                new Tuple<>(selected.getId(),loggedUser.getId()) :
                new Tuple<>(loggedUser.getId(),selected.getId());
        if(this.service.removeFriendship(ids).isEmpty())
            LoggingAlert.showErrorMessage(null,"You are not friends with "+selected.getFirstName()+" "+selected.getLastName());
        else
        {
            LoggingAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success","You are no longer friends");
            update();
        }
    }

    public void handleSendFriendRequest(ActionEvent actionEvent) {
        UserDTO selected = getSelectedUser();
        try {
            if (this.service.sendFriendRequest(loggedUser.getId(), selected.getId()).isPresent())
                LoggingAlert.showErrorMessage(null,"The friend request could not be sent");
            else
                LoggingAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success","The friend request was sent successfully");
        }catch (Exception e){
            LoggingAlert.showErrorMessage(null,e.getMessage());
        }
    }

    public void handleYourFriendRequests(ActionEvent actionEvent) {//TODO
    }

    public void handleTextFieldNameKeyTyped(KeyEvent keyEvent) {
        if(textFieldName.getText().equals(""))
            model.setAll(allUsers);
        model.setAll(this.service.filterUsers(textFieldName.getText(),allUsers));
    }

    private UserDTO getSelectedUser(){
        UserDTO userDTO = tableViewFriendships.getSelectionModel().getSelectedItem();
        if(userDTO==null)
            LoggingAlert.showMessage(null, Alert.AlertType.WARNING,"Attention","You did not select a user");
        return userDTO;
    }

    private void update(){
        allUsers = this.service.getAllUserDTO(loggedUser.getId());
        model.setAll(allUsers);
    }

}
