package socialnetwork.domain.validators;

import socialnetwork.domain.FriendRequest;

//validates from a logical perspective the entities
public class FriendRequestValidator implements Validator<FriendRequest> {

    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        String message="";
        if(!validateIDs(entity))
            message+="Invalid User IDs";
        if(!message.equals(""))
            throw new ValidationException(message);
    }

    private boolean validateIDs(FriendRequest friendRequest){
        return friendRequest.getToUser() != null && friendRequest.getFromUser()!=null;
    }
}
