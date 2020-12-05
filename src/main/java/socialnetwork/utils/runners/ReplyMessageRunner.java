package socialnetwork.utils.runners;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import socialnetwork.controller.MyAllert;
import socialnetwork.service.MasterService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReplyMessageRunner implements Runner {

    private final Long idMessage;
    private final Long idUser;
    private final String text;
    private final MasterService service;

    public ReplyMessageRunner(Long idMessage, Long idUser, String text, MasterService service) {
        this.idMessage = idMessage;
        this.idUser = idUser;
        this.text = text;
        this.service = service;
    }

    @Override
    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            try{
                if(this.service.replyMessage(idMessage,idUser,text).isEmpty())
                    Platform.runLater(()->MyAllert.showMessage(null, Alert.AlertType.CONFIRMATION,"Success","Message replied successfully"));
                else
                    Platform.runLater(()->MyAllert.showErrorMessage(null,"Failed to reply message"));
            }catch (Exception e){
                Platform.runLater(()-> MyAllert.showErrorMessage(null,"Message could not be replied to"));
            }
        });
        executor.shutdown();
    }
}
