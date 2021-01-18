package socialnetwork.service;

import socialnetwork.domain.Message;
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

public class MessageService implements PagingService<Long, Message> {

    private final PagingRepository<Long,Message> repository;

    public MessageService(PagingRepository<Long, Message> repository) {
        this.repository = repository;
    }


    @Override
    public Optional<Message> add(Message entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<Message> remove(Long aLong) {
        return repository.delete(aLong);
    }

    @Override
    public Optional<Message> findOne(Long aLong) {
        return repository.findOne(aLong);
    }

    @Override
    public List<Message> findAll() {
        Iterable<Message> all = repository.findAll();
        return StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
    }

    public Optional<Message> update(Message message){
        return this.repository.update(message);
    }

    @Override
    public List<Message> getEntities(int page){
        Pageable pageable = new PageableImplementation(page,this.pageSize);
        Page<Message> all = repository.findAll(pageable);
        return all.getContent().collect(Collectors.toList());
    }

    public List<Message> getMessagesPage(int leftLimit,int rightLimit,User user1, User user2){
        Long id1= user1.getId(), id2= user2.getId();
        Predicate<Message> predicate = message -> (message.getFrom().equals(id1)
                && message.getTo().contains(id2)) || (message.getFrom().equals(id2)
                && message.getTo().contains(id1));

        return this.findAll().stream()
                .filter(predicate)
                .skip(leftLimit)
                .limit(rightLimit)
                .collect(Collectors.toList());
    }
}
