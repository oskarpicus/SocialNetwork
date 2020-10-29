package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipService implements Service<Tuple<Long,Long>,Friendship> {

    private final Community community;
    private final Repository<Tuple<Long,Long>, Friendship> repo;
    private final Repository<Long, User> userRepository;

    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> repo, Repository<Long, User> userRepository) {
        community = new Community(repo);
        this.repo = repo;
        this.userRepository = userRepository;
    }


    public Optional<Friendship> add(Friendship friendship) {
        // we check if the ids of the friendship refer to users
        Tuple<Long,Long> ids = friendship.getId();
        Optional<User> user1 = this.userRepository.findOne(ids.getLeft());
        if(user1.isEmpty())
            throw new ServiceException("Id "+ids.getLeft()+" does not refer a user");
        Optional<User> user2 = this.userRepository.findOne(ids.getRight());
        if(user2.isEmpty())
            throw new ServiceException("Id "+ids.getRight()+" does not refer a user");
        return repo.save(friendship);
    }

    @Override
    public Optional<Friendship> remove(Tuple<Long, Long> ids) {
        return repo.delete(ids);
    }

    public List<Friendship> findAll(){
        Iterable<Friendship> all = repo.findAll();
        return StreamSupport.stream(all.spliterator(),false).collect(Collectors.toList());
    }

    public int getNumberOfCommunities(){
        return this.community.getNumberOfCommunities();
    }

}
