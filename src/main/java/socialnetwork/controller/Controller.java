package socialnetwork.controller;

import socialnetwork.domain.User;
import socialnetwork.service.MasterServiceWithLogging;

public interface Controller {

    void setService(MasterServiceWithLogging service);

    void openWindow(String name);

    void setLoggedUser(User loggedUser);

    void initialize(MasterServiceWithLogging service,User loggedUser);

    void closeWindow();

}
