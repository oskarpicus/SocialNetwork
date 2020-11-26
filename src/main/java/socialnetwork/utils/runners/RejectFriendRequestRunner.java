package socialnetwork.utils.runners;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import socialnetwork.controller.MyAllert;
import socialnetwork.service.MasterService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RejectFriendRequestRunner implements Runner {

    private final Long requestID;
    private final MasterService service;

    public RejectFriendRequestRunner(Long requestID, MasterService service) {
        this.requestID = requestID;
        this.service = service;
    }

    @Override
    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (this.service.rejectFriendRequest(requestID).isEmpty())
                    Platform.runLater(() ->
                            MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION, "Success", "You successfully rejected the request"));
                else
                    Platform.runLater(() ->
                            MyAllert.showErrorMessage(null, "The request could not be rejected"));
            } catch (Exception e) {
                Platform.runLater(() ->
                        MyAllert.showErrorMessage(null, e.getMessage()));
            }
        });
        executor.shutdown();
    }

}
