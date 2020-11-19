package socialnetwork.repository.file;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.validators.Validator;

import java.util.List;

public class FriendRequestFile extends AbstractFileRepository<Long, FriendRequest> {

    public FriendRequestFile(String fileName, Validator<FriendRequest> validator) {
        super(fileName, validator);
    }

    @Override
    public FriendRequest extractEntity(List<String> attributes) {
        Long id = Long.parseLong(attributes.get(0));
        Long fromId = Long.parseLong(attributes.get(1));
        Long toId = Long.parseLong(attributes.get(2));
        String status = attributes.get(3);
        FriendRequest friendRequest = new FriendRequest(fromId,toId);
        friendRequest.setStatus(status);
        friendRequest.setId(id);
        return friendRequest;
    }

    @Override
    protected String createEntityAsString(FriendRequest entity) {
        return entity.getId()+";"+entity.getFromUser()+";"+entity.getToUser()+";"+entity.getStatus();
    }
}
