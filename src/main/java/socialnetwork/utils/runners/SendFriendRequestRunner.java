package socialnetwork.utils.runners;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import socialnetwork.controller.MyAllert;
import socialnetwork.service.MasterService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendFriendRequestRunner implements Runner{

    private final Long loggedUserId;
    private final Long selectedId;
    private final MasterService service;

    public SendFriendRequestRunner(Long loggedUserId, Long selectedId, MasterService service) {
        this.loggedUserId = loggedUserId;
        this.selectedId = selectedId;
        this.service = service;
    }


    @Override
    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            try{
                if(this.service.sendFriendRequest(loggedUserId, selectedId).isPresent())
                    Platform.runLater(()->
                        MyAllert.showErrorMessage(null,"The friend request could not be sent"));
                else
                    Platform.runLater(()->
                        MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success","The friend request was sent successfully"));
            }catch (Exception e){
                Platform.runLater(()->
                        MyAllert.showErrorMessage(null,"Friend Request could not be sent"));
            }
        });
        executor.shutdown();
    }
}
