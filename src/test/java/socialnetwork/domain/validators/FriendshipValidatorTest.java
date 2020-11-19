package socialnetwork.domain.validators;

import org.junit.Test;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.UserFile;

import static org.junit.Assert.*;

public class FriendshipValidatorTest {

    String fileNameUsers = "data/test/usersTest.csv";

    @Test
    public void validate() {
        FriendshipValidator validator = new FriendshipValidator();
        Friendship friendship = new Friendship();
        Tuple<Long,Long> ids = new Tuple<>(1L,4L);
        friendship.setId(ids);
        validator.validate(friendship);

        ids.setLeft(null);
        ids.setRight(null);
        try{
            validator.validate(friendship);
            fail();
        }catch (ValidationException e ){
            assertEquals("Invalid first ID\nInvalid second ID\n",e.getMessage());
        }

        ids.setRight(4L);
        try{
            validator.validate(friendship);
            fail();
        }catch (ValidationException e ){
            assertEquals("Invalid first ID\n",e.getMessage());
        }

        ids.setRight(null);
        ids.setLeft(4L);
        try{
            validator.validate(friendship);
            fail();
        }catch (ValidationException e ){
            assertEquals("Invalid second ID\n",e.getMessage());
        }

        ids.setRight(4L);
        try{
            validator.validate(friendship);
            fail();
        }catch (ValidationException e){
            assertEquals("IDs cannot be equal\n",e.getMessage());
        }
    }
}