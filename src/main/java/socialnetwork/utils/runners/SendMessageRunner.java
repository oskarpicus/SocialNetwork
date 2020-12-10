package socialnetwork.utils.runners;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import socialnetwork.controller.MyAllert;
import socialnetwork.domain.Message;
import socialnetwork.service.MasterService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendMessageRunner implements Runner {

    private final Message message;
    private final MasterService service;

    public SendMessageRunner(Message message, MasterService service) {
        this.message = message;
        this.service = service;
    }

    @Override
    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            try{
                if(this.service.sendMessage(message).isEmpty())
                    Platform.runLater(()->MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success","Message sent successfully"));
                else
                    Platform.runLater(()->MyAllert.showErrorMessage(null,"Message could not be sent"));
            }catch (Exception e){
                Platform.runLater(()-> MyAllert.showErrorMessage(null,"Could not send message"));
            }
        });
        executor.shutdown();
    }
}
