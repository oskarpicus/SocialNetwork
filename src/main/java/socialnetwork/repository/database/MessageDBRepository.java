package socialnetwork.repository.database;

import socialnetwork.domain.Message;
import socialnetwork.domain.validators.Validator;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDBRepository extends AbstractDBRepository<Long, Message> {


    public MessageDBRepository(Validator<Message> validator, String dataBaseName) {
        super(validator, dataBaseName);
    }

    @Override
    protected String getSaveCommand(Message entity) {
        //the entity is newly created, it can't be replied to
        Long id=entity.getId();
        Long fromUserId = entity.getFrom();
        List<Long> to = entity.getTo();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(entity.getDate());
        String text = entity.getMessage();
        StringBuilder command;
        command = new StringBuilder("INSERT INTO Messages(id,text,data) VALUES (" + id + ",'" + text + "','" + date + "');");
        for(Long aLong : to){
            command.append("INSERT INTO MessagesUsers(fromUserId,toUserId,messageId) VALUES(")
                    .append(fromUserId).append(",")
                    .append(aLong).append(",")
                    .append(id)
                    .append(");");
        }
        return command.toString();
    }

    @Override
    protected String getDeleteCommand(Long aLong) {
        return "DELETE FROM Messages WHERE id="+aLong+";";
    }

    @Override
    protected String getFindOneCommand(Long aLong) {
        return "SELECT fromUserId,toUserId,m.id,text,data,replyId "+
                "FROM Users u "+
                "INNER JOIN MessagesUsers mu ON u.id=mu.fromUserId "+
                "INNER JOIN Messages m ON mu.messageId=m.id "+
                "WHERE m.id="+aLong+";";
    }

    @Override
    protected String getFindAllCommand() {
        return "SELECT fromUserId,toUserId,m.id,text,data,replyId "+
                "FROM Users u "+
                "INNER JOIN MessagesUsers mu ON u.id=mu.fromUserId "+
                "INNER JOIN Messages m ON mu.messageId=m.id " +
                "ORDER BY m.id;";
    }

    @Override
    protected Message extractEntityFromResultSet(ResultSet resultSet) {
        try {
            Long fromUserId = (long) resultSet.getInt("fromUserId");
            Long toUserId= (long) resultSet.getInt("toUserId");
            Long messageId = (long) resultSet.getInt("id");
            String text = resultSet.getString("text");
            Long replyId = (long) resultSet.getInt("replyId");
            if(resultSet.wasNull())
                replyId=null;


            Date time = resultSet.getDate("data");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String timeString=dateFormat.format(time);
            LocalDateTime time1=LocalDateTime.parse(timeString+"T11:50:55");

            //if the message already exists, we only add the toUser and the replyId
            Optional<Message> message = this.getMessageInMemory(messageId);
            if(message.isPresent()){
                message.get().addToUser(toUserId);
                message.get().addReply(replyId);
                return message.get();
            }
            else{ //the message does not exist ==> we create it
                List<Long> toList = new ArrayList<>();
                toList.add(toUserId);
                Message messageResult = new Message(fromUserId,toList,text);
                messageResult.setDate(time1);
                messageResult.addReply(replyId);
                messageResult.setId(messageId);
                Message.setNUMBER_OF_MESSAGES((++messageId));
                return messageResult;
            }

        }catch (Exception e){
            return null;
        }
    }

    @Override
    protected String getUpdateCommand(Message entity) {
        return "UPDATE MessagesUsers SET replyId="+entity.getReply().get(entity.getReply().size()-1)+
                " WHERE messageId="+entity.getId()+" AND toUserId="+entity.getLastReplied()+";";
    }

    private Optional<Message> getMessageInMemory(Long id){
        return Optional.ofNullable(super.allEntities.get(id));
    }

}
