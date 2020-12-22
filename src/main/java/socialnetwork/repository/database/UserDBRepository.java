package socialnetwork.repository.database;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;

import java.sql.ResultSet;


public class UserDBRepository extends AbstractDBRepository<Long,User> {

    public UserDBRepository(Validator<User> validator,String dataBaseName) {
        super(validator,dataBaseName);
    }


    @Override
    protected String getSaveCommand(User entity) {
        return "INSERT INTO Users(id,firstName,secondName,password,userName) "
                + "VALUES ("+entity.getId()+",'"+entity.getFirstName()+"', '"
                + entity.getLastName()+"','"+entity.getPassword()+"','"+entity.getUserName()+"');";
    }

    @Override
    protected String getDeleteCommand(Long id) {
        return "DELETE FROM Users WHERE id ="+id+";";
    }

    @Override
    protected String getFindOneCommand(Long id) {
        return "SELECT * FROM Users WHERE id ="+id+";";
    }

    @Override
    protected String getFindAllCommand() {
        return "SELECT * FROM Users ORDER BY id;";
    }

    @Override
    protected User extractEntityFromResultSet(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("firstName");
            String secondName = resultSet.getString("secondName");
            String userName = resultSet.getString("userName");
            String password = resultSet.getString("password");
            User user = new User(firstName,secondName,userName,password);
            user.setId((long)id);
            id++;
            User.setNUMBEROFUSERS((long)id);
            return user;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected String getUpdateCommand(User entity) {
        return "UPDATE Users "+
                "SET firstname='"+entity.getFirstName()+"',lastname='"+entity.getLastName()+"' "+
                "WHERE id="+entity.getId()+";";
    }


}
