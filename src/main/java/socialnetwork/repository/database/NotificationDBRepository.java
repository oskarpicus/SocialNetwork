package socialnetwork.repository.database;

import socialnetwork.domain.Notification;
import socialnetwork.domain.validators.Validator;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class NotificationDBRepository extends AbstractDBRepository<Long, Notification> {
    public NotificationDBRepository(Validator<Notification> validator, String dataBaseName) {
        super(validator, dataBaseName);
    }

    @Override
    protected String getSaveCommand(Notification entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(entity.getDate());
        StringBuilder result = new StringBuilder("INSERT INTO Notifications(id,text,date) VALUES " +
                "(" + entity.getId() + ",'" + entity.getText() + "','" + formattedDate + "'); ");
        //we add the receivers
        if(!entity.getReceivers().isEmpty())
            result.append("INSERT INTO NotificationsUsers(idNotification, idUser) VALUES ");
        boolean previous = false;
        for(Long idUser : entity.getReceivers()){
            if(previous){
                result.append(",");
            }
            result.append("(").append(entity.getId()).append(",").append(idUser).append(")");
            previous=true;
        }
        result.append(";");
        return result.toString();
    }

    @Override
    protected String getDeleteCommand(Long aLong) {
        return "DELETE FROM Notifications where id="+aLong+";";
    }

    @Override
    protected String getFindOneCommand(Long aLong) {
        return "SELECT id,text,date,idUser "+
                "FROM Notifications INNER JOIN NotificationsUsers ON id=idNotification "+
                "WHERE id="+aLong+";";
    }

    @Override
    protected String getFindAllCommand() {
        return "SELECT id,text,date,idUser "+
                "FROM Notifications INNER JOIN NotificationsUsers ON id=idNotification "+
                "ORDER BY id";
    }

    @Override
    protected Notification extractEntityFromResultSet(ResultSet resultSet) {
        try{
            Long id = (long)resultSet.getInt("id");
            String text = resultSet.getString("text");
            LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
            Long idUser = (long)resultSet.getInt("idUser");

            Optional<Notification> optionalNotification = getNotificationInMemory(id);
            Notification notification;
            if(optionalNotification.isEmpty()){ //we create it
                notification = new Notification(text,date, List.of(idUser));
                notification.setId(id);
                Notification.setNumberOfNotifications(id+1);
            }
            else {
                notification = optionalNotification.get();
                notification.getReceivers().add(idUser);
            }
            return notification;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    protected String getUpdateCommand(Notification entity) {
        return null;
    }

    private Optional<Notification> getNotificationInMemory(Long id){
        return Optional.ofNullable(super.allEntities.get(id));
    }
}
