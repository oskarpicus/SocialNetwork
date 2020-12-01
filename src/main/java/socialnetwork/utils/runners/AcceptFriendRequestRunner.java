package socialnetwork.utils.runners;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import socialnetwork.controller.MyAllert;
import socialnetwork.service.MasterService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AcceptFriendRequestRunner implements Runner {

    private final Long requestID;
    private final MasterService service;

    public AcceptFriendRequestRunner(Long requestID, MasterService service) {
        this.requestID = requestID;
        this.service = service;
    }

    @Override
    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
       // executor.execute(() -> {
            try {
                if (this.service.acceptFriendRequest(requestID).isEmpty())
                    Platform.runLater(() ->
                            MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION, "Success", "You successfully accepted the request"));
                else
                    Platform.runLater(() ->
                            MyAllert.showErrorMessage(null, "The request could not be accepted"));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() ->
                        MyAllert.showErrorMessage(null, e.getMessage()));
            }
     //   });
        executor.shutdown();
    }

}
