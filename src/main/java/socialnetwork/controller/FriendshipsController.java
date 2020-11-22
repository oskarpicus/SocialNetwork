package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import socialnetwork.domain.User;
import socialnetwork.service.MasterService;

public class FriendshipsController {

    private MasterService service;
    private User loggedUser;

    @FXML
    Label labelGreeting;

    public void initialize(MasterService service,User loggedUser){
        this.service=service;
        this.loggedUser=loggedUser;
        labelGreeting.setText("Hello "+loggedUser.getFirstName()+" "+loggedUser.getLastName()+" !");
    }
}
