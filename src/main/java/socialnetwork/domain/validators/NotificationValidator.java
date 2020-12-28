package socialnetwork.domain.validators;

import socialnetwork.domain.Notification;

public class NotificationValidator implements Validator<Notification> {
    @Override
    public void validate(Notification entity) throws ValidationException {
        String errors = "";
        if(!validateText(entity))
            errors+="Invalid text\n";
        if(!validateDate(entity))
            errors+="Invalid date\n";
        if(!errors.equals(""))
            throw new ValidationException(errors);
    }

    private boolean validateText(Notification notification){
        return !notification.getText().equals("");
    }

    private boolean validateDate(Notification notification){
        return notification.getDate()!=null;
    }
}
