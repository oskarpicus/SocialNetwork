package socialnetwork.utils.runners;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import socialnetwork.controller.MyAllert;
import socialnetwork.domain.Notification;
import socialnetwork.service.MasterService;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SendNotificationRunner implements Runnable {

    private final MasterService service;
    private boolean success = false;

    public SendNotificationRunner(MasterService service) {
        this.service = service;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("new cycle");
            success=false;
            service.getAllEvents().stream()
                    .filter(event -> {
                        long time = ChronoUnit.DAYS.between(LocalDateTime.now(),event.getDate());
                        return time <= Constants.DAYS_FOR_EVENT_NOTIFICATION && time>=0;
                    })
                    .forEach(event -> {
                        if(!event.getParticipants().isEmpty() && !event.getSubscribedToNotification().isEmpty()) {
                            String text = event.getName() + " is in " + Constants.DAYS_FOR_EVENT_NOTIFICATION + " day, on " + event.getDate();
                            Notification notification = new Notification(text);
                            event.getParticipants().forEach(id->{
                                if(service.sendNotificationForEvent(event,notification,id).isEmpty())
                                    success=true;
                            });
                        }
                    });
            if(success)
                Platform.runLater(()->MyAllert.showMessage(null, Alert.AlertType.INFORMATION,"Notification","You have received a notification"));
            try {
                Thread.sleep(60000/2); //1 minute
            }catch (InterruptedException e){
                break;
            }
    }

}
}
