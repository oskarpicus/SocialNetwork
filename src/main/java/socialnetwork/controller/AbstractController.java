package socialnetwork.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.MasterServiceWithLogging;

import java.io.IOException;

public abstract class AbstractController implements Controller{

    protected MasterServiceWithLogging service;
    protected User loggedUser;

    @Override
    public void setService(MasterServiceWithLogging service){
        this.service=service;
    }

    @Override
    public void openWindow(String name){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/"+name+".fxml"));
            Pane pane = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle(name.substring(0,1).toUpperCase()+name.substring(1));

            Controller controller = loader.getController();
            controller.initialize(service,loggedUser);
            closeWindow();
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void setLoggedUser(User loggedUser){
        this.loggedUser=loggedUser;
    }

    @Override
    public void initialize(MasterServiceWithLogging service, User loggedUser) {
        setLoggedUser(loggedUser);
        setService(service);
    }
}
