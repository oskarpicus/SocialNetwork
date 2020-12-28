package socialnetwork.service;

import socialnetwork.domain.Event;
import socialnetwork.domain.Tuple;
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

    public boolean isSubscribedToNotification(Long idEvent, Long idUser){
        Optional<Event> event = findOne(idEvent);
        if(event.isEmpty())
            return false;
        return event.get().getSubscribedToNotification().contains(idUser);
    }

    public Optional<Event> addParticipant(Long idEvent, Long idUser){
        Optional<Event> event = findOne(idEvent);
        if(event.isEmpty())
            return Optional.empty();
        if(event.get().getParticipants().contains(idUser))
            return event;
        event.get().getParticipants().add(idUser);
        event.get().getSubscribedToNotification().add(idUser);
        return repository.update(event.get());
    }

    public Optional<Event> removeParticipant(Long idEvent, Long idUser){
        Optional<Event> event = findOne(idEvent);
        if(event.isEmpty())
            return Optional.empty();
        if(!event.get().getParticipants().contains(idUser))
            return event;
        event.get().getParticipants().remove(idUser);
        //we remove the subscription
        event.get().getSubscribedToNotification().remove(idUser);
        return repository.update(event.get());
    }

    public Optional<Event> addSubscriber(Event event, Long idUser){
        if(event.getSubscribedToNotification().contains(idUser)){ //the user is already subscribed
            return Optional.of(event);
        }
        event.getSubscribedToNotification().add(idUser);
        return repository.update(event);
    }

    public Optional<Event> removeSubscriber(Event event,Long idUser){
        if(!event.getSubscribedToNotification().contains(idUser)){ //the user wasn't subscribed in the first place
            return Optional.of(event);
        }
        event.getSubscribedToNotification().remove(idUser);
        return repository.update(event);
    }
}
