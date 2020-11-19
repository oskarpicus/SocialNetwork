package socialnetwork.domain.validators;

import socialnetwork.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String message="";
        if(!validateFirstName(entity))
            message=message.concat("Invalid First Name\n");
        if(!validateLastName(entity))
            message=message.concat("Invalid Last Name\n");
        if(!message.equals(""))
            throw new ValidationException(message);
    }

    private boolean validateFirstName(User entity){
        return !entity.getFirstName().equals("");
    }

    private boolean validateLastName(User entity){
        return !entity.getLastName().equals("");
    }
}
