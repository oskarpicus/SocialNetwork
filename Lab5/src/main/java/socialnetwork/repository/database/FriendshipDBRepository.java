package socialnetwork.repository.database;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;
import java.sql.*;

public class FriendshipDBRepository extends AbstractDBRepository<Tuple<Long,Long>, Friendship> {

    public FriendshipDBRepository(Validator<Friendship> validator,String dataBaseName) {
        super(validator,dataBaseName);
    }


    @Override
    protected String getSaveCommand(Friendship entity) {
        Tuple<Long,Long> ids = entity.getId();
        return "INSERT INTO Friendships(id1,id2) VALUES ("+ids.getLeft()
                +","+ids.getRight()+");";
    }

    @Override
    protected String getDeleteCommand(Tuple<Long, Long> id) {
        return "DELETE FROM Friendships WHERE id1="+id.getLeft()+" AND id2="+id.getRight()+";";
    }

    @Override
    protected String getFindOneCommand(Tuple<Long, Long> id) {
        return "SELECT * FROM Friendships "+
                "WHERE id1="+id.getLeft()+" AND id2="+id.getRight()+";";
    }

    @Override
    protected String getFindAllCommand() {
        return "SELECT * FROM Friendships;";
    }

    @Override
    protected Friendship extractEntityFromResultSet(ResultSet resultSet) {
        try{
            int id1 = resultSet.getInt("id1");
            int id2 = resultSet.getInt("id2");
            Friendship friendship = new Friendship();
            friendship.setId(new Tuple<>((long)id1,(long)id2));
            return friendship;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


//    @Override
//    public Optional<Friendship> findOne(Tuple<Long, Long> longLongTuple) {
//        try{
//            Statement statement = c.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM Friendships f WHERE " +
//                    "(f.id1="+longLongTuple.getRight()+" AND f.id2="+longLongTuple.getLeft()+
//                    ")OR (f.id1="+longLongTuple.getLeft()+" AND f.id2="+longLongTuple.getRight()+");");
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return Optional.empty();
//    }


}
