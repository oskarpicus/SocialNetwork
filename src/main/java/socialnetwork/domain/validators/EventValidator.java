package socialnetwork.domain.validators;

import socialnetwork.domain.Event;

public class EventValidator implements Validator<Event> {
    @Override
    public void validate(Event entity) throws ValidationException {
        String errors = "";
        if(!validateName(entity))
            errors+="Invalid Name\n";
        if(!validateLocation(entity))
            errors+="Invalid Location\n";
        if(!validateDescription(entity))
            errors+="Invalid Description\n";
        if(!errors.equals(""))
            throw new ValidationException(errors);
    }

    private boolean validateName(Event event){
        return !event.getName().equals("");
    }

    private boolean validateLocation(Event event){
        return !event.getLocation().equals("");
    }

    private boolean validateDescription(Event event){
        return !event.getDescription().equals("");
    }

}
