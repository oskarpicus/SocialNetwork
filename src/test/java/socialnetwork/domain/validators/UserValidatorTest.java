package socialnetwork.domain.validators;

import org.junit.Test;
import socialnetwork.domain.User;

import static org.junit.Assert.*;

public class UserValidatorTest {

    @Test
    public void validate() {
        UserValidator valid = new UserValidator();
        User user1 = new User("","");
        try{
            valid.validate(user1);
            fail();
        }catch (ValidationException e){
            assertEquals("Invalid First Name\nInvalid Last Name\n",e.getMessage());
        }
        User user2 = new User("a","");
        try{
            valid.validate(user2);
            fail();
        }catch(ValidationException e){
            assertEquals("Invalid Last Name\n",e.getMessage());
        }
        User user3 = new User("","b");
        try{
            valid.validate(user3);
            fail();
        }catch (ValidationException e){
            assertEquals("Invalid First Name\n",e.getMessage());
        }
        User user4 = new User("a","b");
        try{
            valid.validate(user4);
        }catch (ValidationException e){
            fail();
        }
    }
}