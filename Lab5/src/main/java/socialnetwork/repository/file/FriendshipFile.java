package socialnetwork.repository.file;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        LocalDateTime time=LocalDateTime.parse(attributes.get(2)+"T11:50:55");
        friendship.setDate(time);
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        Tuple<Long,Long> ids = entity.getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ids.getLeft()+";"+ids.getRight()+";"+formatter.format(entity.getDate());
    }
}
