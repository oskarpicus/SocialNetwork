package socialnetwork.service;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.Pageable;
import socialnetwork.repository.paging.PageableImplementation;
import socialnetwork.repository.paging.PagingRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendRequestService implements PagingService<Long, FriendRequest> {

    private final PagingRepository<Long,FriendRequest> repository;

    public FriendRequestService(PagingRepository<Long, FriendRequest> repository) {
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
     * Method for removing a friend request which is pending
     * @param id : id of the friend request to be removed
     * @return an {@code Optional}
     *              -null if there is no friend request with that id or it is not pending
     *              -the entity, otherwise
     */
    public Optional<FriendRequest> removePendingFriendRequest(Long id){
        Optional<FriendRequest> request = this.repository.findOne(id);
        if(request.isPresent() && request.get().getStatus().equals("pending"))
            return this.repository.delete(id);
        return Optional.empty();
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


    @Override
    public List<FriendRequest> getEntities(int page){
        Pageable pageable = new PageableImplementation(page,pageSize);
        Page<FriendRequest> all = repository.findAll(pageable);
        return all.getContent().collect(Collectors.toList());
    }

    public List<FriendRequest> getSentFriendRequestsPage(int pageNumber, User user){
        return getFriendRequestsPage(pageNumber,request -> request.getFromUser().equals(user.getId()));
    }

    public List<FriendRequest> getReceivedFriendRequestsPage(int pageNumber, User user){
        return getFriendRequestsPage(pageNumber,request -> request.getToUser().equals(user.getId()));
    }

    private List<FriendRequest> getFriendRequestsPage(int pageNumber, Predicate<FriendRequest> predicate){
        return this.findAll().stream()
                .filter(predicate)
                .skip(pageNumber  * PagingService.pageSize)
                .limit(PagingService.pageSize)
                .collect(Collectors.toList());
    }

}
