package socialnetwork.service;

import socialnetwork.domain.Event;
import socialnetwork.repository.paging.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EventService implements PagingService<Long, Event> {

    private final PagingRepository<Long,Event> repository;

    public EventService(PagingRepository<Long, Event> repository) {
        this.repository = repository;
    }

    @Override
    public List<Event> getEntities(int page) {
        Pageable pageable = new PageableImplementation(page,pageSize);
        Page<Event> all = repository.findAll(pageable);
        return all.getContent().collect(Collectors.toList());
    }

    @Override
    public Optional<Event> add(Event entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<Event> remove(Long aLong) {
        return repository.delete(aLong);
    }

    @Override
    public Optional<Event> findOne(Long aLong) {
        return repository.findOne(aLong);
    }

    @Override
    public List<Event> findAll() {
        Iterable<Event> all = repository.findAll();
        return StreamSupport.stream(all.spliterator(),false).collect(Collectors.toList());
    }
}
