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
        return this.findAll().stream()
                .skip(page*PagingService.pageSize)
                .limit(PagingService.pageSize)
                .collect(Collectors.toList());
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
        return StreamSupport.stream(all.spliterator(),false)
                .sorted((event1,event2)->event2.getDate().compareTo(event1.getDate()))
                .collect(Collectors.toList());
    }

    public boolean isParticipant(Long idEvent, Long idUser){
        Optional<Event> event = findOne(idEvent);
        if(event.isEmpty())
            return false;
        return event.get().getParticipants().contains(idUser);
    }
}
