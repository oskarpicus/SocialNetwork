package socialnetwork.repository.database;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.validators.Validator;

import java.sql.Date;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class FriendRequestDBRepository extends AbstractDBRepository<Long, FriendRequest> {

    public FriendRequestDBRepository(Validator<FriendRequest> validator, String dataBaseName) {
        super(validator, dataBaseName);
    }

    @Override
    protected String getSaveCommand(FriendRequest entity) {
        return "INSERT INTO FriendRequests(id,fromUser,toUser,status,date) VALUES ("+
               +entity.getId()+","+entity.getFromUser()+","+entity.getToUser()+",'"+entity.getStatus()+"','"+
                entity.getDate()+"');";
    }

    @Override
    protected String getDeleteCommand(Long aLong) {
        return "DELETE FROM FriendRequests WHERE id="+aLong+";";
    }

    @Override
    protected String getFindOneCommand(Long aLong) {
        return "SELECT * FROM FriendRequests WHERE id="+aLong+";";
    }

    @Override
    protected String getFindAllCommand() {
        return "SELECT * FROM FriendRequests ORDER BY id;";
    }

    @Override
    protected FriendRequest extractEntityFromResultSet(ResultSet resultSet) {
        try{
            int id=resultSet.getInt("id");
            int fromUser=resultSet.getInt("fromuser");
            int toUser=resultSet.getInt("touser");
            String status=resultSet.getString("status");
            Date time = resultSet.getDate("date");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String timeString=dateFormat.format(time);
            LocalDateTime time1=LocalDateTime.parse(timeString+"T11:50:55");
            FriendRequest friendRequest = new FriendRequest((long)fromUser,(long)toUser);
            friendRequest.setId((long)id);
            friendRequest.setStatus(status);
            friendRequest.setDate(time1);
            FriendRequest.setNumberOfFriendRequests(((long)++id));
            return friendRequest;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected String getUpdateCommand(FriendRequest entity) {
        return "UPDATE FriendRequests"+
                " SET fromUser="+entity.getFromUser()+",toUser="+entity.getToUser()+",status='"+entity.getStatus()+
                "' WHERE id="+entity.getId()+";";
    }
}
