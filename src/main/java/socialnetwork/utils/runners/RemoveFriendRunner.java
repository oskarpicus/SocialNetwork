package socialnetwork.utils.runners;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import socialnetwork.controller.MyAllert;
import socialnetwork.domain.Tuple;
import socialnetwork.service.MasterService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoveFriendRunner implements Runner {

    private final Tuple<Long,Long> ids;
    private final MasterService service;

    public RemoveFriendRunner(Tuple<Long,Long> ids, MasterService service) {
        this.ids=ids;
        this.service = service;
    }

    @Override
    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            try{
                if(this.service.removeFriendship(ids).isEmpty())
                    Platform.runLater(()->
                        MyAllert.showErrorMessage(null,"You are not friends with this user"));
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
