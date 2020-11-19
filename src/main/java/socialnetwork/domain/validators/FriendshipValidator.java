package socialnetwork.domain.validators;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;

public class FriendshipValidator implements Validator<Friendship> {


    public FriendshipValidator(){

    }

    @Override
    public void validate(Friendship entity) throws ValidationException {
        String message = "";

        if(!validateId1ForNull(entity))
            message=message.concat("Invalid first ID\n");
        if(!validateId2ForNull(entity))
            message=message.concat("Invalid second ID\n");

        if(!message.equals(""))
            throw new ValidationException(message);

        if(entity.getId().getLeft().equals(entity.getId().getRight()))
            throw new ValidationException("IDs cannot be equal\n");

    }

    private boolean validateId1ForNull(Friendship entity){
        Tuple<Long,Long> ids = entity.getId();
        return ids.getLeft()!=null;
    }

    private boolean validateId2ForNull(Friendship entity){
        Tuple<Long,Long> ids = entity.getId();
        return ids.getRight()!=null;
    }

}
