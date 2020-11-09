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
        return "INSERT INTO Users(id,firstName,secondName) "
                + "VALUES ("+entity.getId()+",'"+entity.getFirstName()+"', '"
                + entity.getLastName()+"');";
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
        return "SELECT * FROM Users;";
    }

    @Override
    protected User extractEntityFromResultSet(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("firstName");
            String secondName = resultSet.getString("secondName");
            User user = new User(firstName,secondName);
            user.setId((long)id);
            id++;
            User.setNUMBEROFUSERS((long)id);
            return user;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
