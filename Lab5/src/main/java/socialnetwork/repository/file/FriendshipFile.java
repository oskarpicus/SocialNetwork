package socialnetwork.repository.file;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.util.List;

public class FriendshipFile extends AbstractFileRepository<Tuple<Long,Long>, Friendship> {

    public FriendshipFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship friendship = new Friendship();
        Long id1 = Long.parseLong(attributes.get(0));
        Long id2 = Long.parseLong(attributes.get(1));
        friendship.setId(new Tuple<>(id1,id2));
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        Tuple<Long,Long> ids = entity.getId();
        return ids.getLeft()+";"+ids.getRight();
    }
}
