package socialnetwork.controller;

import socialnetwork.domain.User;
import socialnetwork.service.MasterService;

public interface Controller {

    void setService(MasterService service);

    void openWindow(String name);

    void setLoggedUser(User loggedUser);

    void initialize(MasterService service,User loggedUser);

    void closeWindow();

}
