package socialnetwork.utils.runners;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import socialnetwork.controller.MyAllert;
import socialnetwork.service.MasterService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemovePendingFriendRequestRunner implements Runner{

    private final MasterService service;
    private final Long id;

    public RemovePendingFriendRequestRunner(MasterService service, Long id) {
        this.service = service;
        this.id = id;
    }

    @Override
    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            try{
                if(this.service.removePendingFriendRequest(this.id).isPresent())
                    Platform.runLater(()->MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success","The request was successfully removed"));
                else
                    Platform.runLater(()->MyAllert.showErrorMessage(null,"Failed to remove the request"));
            }catch (Exception e){
                Platform.runLater(()-> MyAllert.showErrorMessage(null,"The request could not be removed"));
            }
        });
        executor.shutdown();
    }
}
