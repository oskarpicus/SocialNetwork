package socialnetwork.service;

import socialnetwork.domain.FriendRequest;
import socialnetwork.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendRequestService implements Service<Long, FriendRequest> {

    private final Repository<Long,FriendRequest> repository;

    public FriendRequestService(Repository<Long, FriendRequest> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<FriendRequest> add(FriendRequest entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<FriendRequest> remove(Long aLong) {
        return repository.delete(aLong);
    }

    @Override
    public Optional<FriendRequest> findOne(Long aLong) {
        return repository.findOne(aLong);
    }

    public List<FriendRequest> findAll(){
        Iterable<FriendRequest> all = repository.findAll();
        return StreamSupport.stream(all.spliterator(),false).collect(Collectors.toList());
    }

    /**
     * Method for accepting a friend request
     * @param id : Long, id of the friend request to be accepted
     * @return an {@code Optional}
     *              - empty, if it was successfully accepted
     *              - otherwise, the entity
     */
    public Optional<FriendRequest> acceptFriendRequest(Long id){
        Optional<FriendRequest> friendRequest = this.repository.findOne(id);
        if(friendRequest.isPresent()){ //if it exists
            if(friendRequest.get().getStatus().equals("pending")){
                friendRequest.get().setStatus("accepted");
                return this.repository.update(friendRequest.get()); //returns empty if it was successfully updated
            }
        }
        FriendRequest request = new FriendRequest();
        request.setId(id);
        return Optional.of(request);
    }

    /**
     * Method for rejecting a friend request
     * @param id : Long, id of the friend request to be rejected
     * @return an {@code Optional}
     *              - empty, if it was successfully rejected
     *              - otherwise, the entity
     */
    public Optional<FriendRequest> rejectFriendRequest(Long id){
        Optional<FriendRequest> friendRequest = this.repository.findOne(id);
        if(friendRequest.isPresent()){
            if(friendRequest.get().getStatus().equals("pending")){
                friendRequest.get().setStatus("rejected");
                return this.repository.update(friendRequest.get());
            }
        }
        FriendRequest request = new FriendRequest();
        request.setId(id);
        return Optional.of(request);
    }
}
