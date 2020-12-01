package socialnetwork.utils.runners;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import socialnetwork.controller.MyAllert;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.service.MasterService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoveFriendRunner implements Runner {

    private final Long loggedUserId;
    private final User selected;
    private final MasterService service;

    public RemoveFriendRunner(Long loggedUserId, User selected, MasterService service) {
        this.loggedUserId = loggedUserId;
        this.selected = selected;
        this.service = service;
    }

    @Override
    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            try{
                Tuple<Long,Long> ids = selected.getId() < loggedUserId ?
                        new Tuple<>(selected.getId(),loggedUserId) :
                        new Tuple<>(loggedUserId,selected.getId());
                if(this.service.removeFriendship(ids).isEmpty())
                    Platform.runLater(()->
                        MyAllert.showErrorMessage(null,"You are not friends with "+selected.getFirstName()+" "+selected.getLastName()));
                else
                    Platform.runLater(()->
                    MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success","You are no longer friends"));
            }catch (Exception e){
                Platform.runLater(()->
                        MyAllert.showErrorMessage(null,e.getMessage()));
            }
        });
        executor.shutdown();
    }
}
