package socialnetwork.service;

import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageService implements Service<Long, Message> {

    private final Repository<Long,Message> repository;

    public MessageService(Repository<Long, Message> repository) {
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
}
