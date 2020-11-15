package socialnetwork.domain.validators;

import socialnetwork.domain.Message;

import java.util.Objects;

public class MessageValidator implements Validator<Message> {

    @Override
    public void validate(Message entity) throws ValidationException {
        String message = "";
        if(!validateIdFrom(entity))
            message=message+"Invalid Id From\n";
        if(!validateIdTo(entity))
            message=message+"Invalid Ids To";
        if(!validateText(entity))
            message=message+"Invalid content of the message\n";
        if(!message.equals(""))
            throw new ValidationException(message);
    }

    private boolean validateIdFrom(Message entity){
        return entity.getFrom()!=null;
    }

    private boolean validateIdTo(Message entity){
        return entity.getTo()
                .stream()
                .allMatch(Objects::nonNull);
    }

    private boolean validateText(Message entity){
        return !entity.getMessage().equals("");
    }
}
