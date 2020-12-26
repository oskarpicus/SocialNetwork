package socialnetwork.controller;

import socialnetwork.controller.pages.PageActions;

public interface Controller {

    void openWindow(String name);

    void initialize(PageActions pageActions);

    void closeWindow();

}
