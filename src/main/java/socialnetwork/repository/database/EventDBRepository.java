package socialnetwork.repository.database;

import socialnetwork.domain.Event;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class EventDBRepository extends AbstractDBRepository<Long, Event> {

    public EventDBRepository(Validator<Event> validator, String dataBaseName) {
        super(validator, dataBaseName);
    }

    @Override
    protected String getSaveCommand(Event entity) { //no participants yet
        Long id = entity.getId();
        String name = entity.getName();
        String description = entity.getDescription();
        LocalDateTime date = entity.getDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(date);
        String location = entity.getLocation();
        return "INSERT INTO Events(id,name,description,date,location) "+
                "VALUES("+id+",'"+name+"','"+description+"','"+formattedDate+"','"+location+"');";
    }

    @Override
    protected String getDeleteCommand(Long aLong) {
        return "DELETE FROM Events where id="+aLong+";";
    }

    @Override
    protected String getFindOneCommand(Long aLong) {
        return "SELECT e.id,e.name,e.location,e.description,e.date,eu.idUser,"+
                        "eu.subscribedToNotification,eu.receivedNotification "+
                "FROM Events e LEFT OUTER JOIN EventsUsers eu ON e.id=eu.idEvent"+
                "WHERE e.id="+aLong+";";
    }

    @Override
    protected String getFindAllCommand() {
        return "SELECT e.id,e.name,e.location,e.description,e.date,eu.idUser,"+
                        "eu.subscribedToNotification,eu.receivedNotification "+
                "FROM Events e LEFT OUTER JOIN EventsUsers eu ON e.id=eu.idEvent "+
                "ORDER BY e.id";
    }

    @Override
    protected Event extractEntityFromResultSet(ResultSet resultSet) {
        try{
            Long id = (long)resultSet.getInt("id");
            String name = resultSet.getString("name");
            String location = resultSet.getString("location");
            String description = resultSet.getString("description");
            LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
            Long idUser = (long)resultSet.getInt("idUser");
            if(resultSet.wasNull())
                idUser=null;
            Boolean subscribedToNotification = resultSet.getBoolean("subscribedToNotification");
            Boolean receivedNotification = resultSet.getBoolean("receivedNotification");

            Optional<Event> optionalEvent = getEventInMemory(id);
            Event event;
            if(optionalEvent.isEmpty()){ //we create the event
                event = new Event(name,date,description,location);
                event.setId(id);
                Event.setNumberOfEvents(id+1);
            }
            else{
                event = optionalEvent.get();
            }
            //we set the other data
            if(idUser!=null) {
                event.getParticipants().add(idUser);
                if(subscribedToNotification)
                    event.getSubscribedToNotification().add(idUser);
                if(receivedNotification)
                    event.getReceivedNotification().add(idUser);
            }
            return event;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    protected String getUpdateCommand(Event entity) {
        StringBuilder result = new StringBuilder();
        result.append("DELETE FROM EventsUsers where idEvent=").append(entity.getId()).append(";");
        for(Long idParticipant : entity.getParticipants()){
            boolean subscribed = entity.getSubscribedToNotification().contains(idParticipant);
            boolean received = entity.getReceivedNotification().contains(idParticipant);

            result.append("INSERT INTO EventsUsers(idEvent,idUser,subscribedToNotification,receivedNotification) " + "VALUES(")
                    .append(entity.getId())
                    .append(",")
                    .append(idParticipant)
                    .append(",")
                    .append(subscribed)
                    .append(",")
                    .append(received)
                    .append(");");
        }
        return result.toString();
    }

    private Optional<Event> getEventInMemory(Long id){
        return Optional.ofNullable(super.allEntities.get(id));
    }
}
