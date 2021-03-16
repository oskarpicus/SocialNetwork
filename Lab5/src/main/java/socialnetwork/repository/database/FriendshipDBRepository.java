package socialnetwork.repository.database;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FriendshipDBRepository extends AbstractDBRepository<Tuple<Long,Long>, Friendship> {

    public FriendshipDBRepository(Validator<Friendship> validator, String dataBaseName) {
        super(validator, dataBaseName);
    }


    @Override
    protected String getSaveCommand(Friendship entity) {
        Tuple<Long, Long> ids = entity.getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(entity.getDate());
        return "INSERT INTO Friendships(id1,id2,date) VALUES (" + ids.getLeft()
                + "," + ids.getRight() + ",'"+date+"');";
    }

    @Override
    protected String getDeleteCommand(Tuple<Long, Long> id) {
        return "DELETE FROM Friendships WHERE id1=" + id.getLeft() + " AND id2=" + id.getRight() + ";";
    }

    @Override
    protected String getFindOneCommand(Tuple<Long, Long> id) {
        return "SELECT * FROM Friendships " +
                "WHERE id1=" + id.getLeft() + " AND id2=" + id.getRight() + ";";
    }

    @Override
    protected String getFindAllCommand() {
        return "SELECT * FROM Friendships;";
    }

    @Override
    protected Friendship extractEntityFromResultSet(ResultSet resultSet) {
        try {
            int id1 = resultSet.getInt("id1");
            int id2 = resultSet.getInt("id2");
            Date time = resultSet.getDate("date");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String timeString=dateFormat.format(time);
            LocalDateTime time1=LocalDateTime.parse(timeString+"T11:50:55");
            Friendship friendship = new Friendship();
            friendship.setDate(time1);
            friendship.setId(new Tuple<>((long) id1, (long) id2));
            return friendship;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected String getUpdateCommand(Friendship entity) {
        Tuple<Long,Long> ids = entity.getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(entity.getDate());
        return "UPDATE Friendships "+
                "SET id1="+ids.getLeft()+",id2="+ids.getRight()+",date='"+date+"' "+
                "WHERE id1="+ids.getLeft()+" AND id2="+ids.getRight()+";";
    }

}