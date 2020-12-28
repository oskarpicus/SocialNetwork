package socialnetwork.service;

import socialnetwork.domain.Notification;
import socialnetwork.domain.User;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.Pageable;
import socialnetwork.repository.paging.PageableImplementation;
import socialnetwork.repository.paging.PagingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NotificationService implements PagingService<Long, Notification> {

    private final PagingRepository<Long,Notification> repository;

    public NotificationService(PagingRepository<Long, Notification> repository) {
        this.repository = repository;
    }

    @Override
    public List<Notification> getEntities(int page) {
        Pageable pageable = new PageableImplementation(page,pageSize);
        Page<Notification> all = repository.findAll(pageable);
        return all.getContent().collect(Collectors.toList());
    }

    @Override
    public Optional<Notification> add(Notification entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<Notification> remove(Long aLong) {
        return repository.delete(aLong);
    }

    @Override
    public Optional<Notification> findOne(Long aLong) {
        return repository.findOne(aLong);
    }

    @Override
    public List<Notification> findAll() {
        Iterable<Notification> all = repository.findAll();
        return StreamSupport.stream(all.spliterator(),false)
                .sorted((note1,note2)->note2.getDate().compareTo(note1.getDate()))
                .collect(Collectors.toList());
    }

    public List<Notification> getNotificationsPage(int pageNumber, User user){
        return this.findAll().stream()
                .filter(notification -> notification.getReceivers().contains(user.getId()))
                .skip(pageNumber*pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    public List<Notification> getNotifications(User user){
        return this.findAll().stream()
                .filter(notification -> notification.getReceivers().contains(user.getId()))
                .collect(Collectors.toList());
    }
}
