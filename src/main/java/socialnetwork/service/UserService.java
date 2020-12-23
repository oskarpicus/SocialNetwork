package socialnetwork.service;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.Pageable;
import socialnetwork.repository.paging.PageableImplementation;
import socialnetwork.repository.paging.PagingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class UserService implements PagingService<Long,User>{
    private final PagingRepository<Long, User> repo;

    public UserService(PagingRepository<Long, User> repo) {
        this.repo = repo;
    }

    /**
     * Method for adding a new user
     * @param user : User, user to be added
     * @return an {@code Optional} - null if the user was saved,
     *                             - the user (id already exists)
     */
    public Optional<User> add(User user) {
        return repo.save(user);
    }

    /**
     * Method for obtaining all the saved users
     * @return : list : List<User>, which stores all the saved users
     */
    public List<User> findAll() {
        //este echivalent cu copiatul tuturor studentilor intr-o lista
        Iterable<User> all = repo.findAll();
        return StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
    }

    public List<User> filterUsersName(String s) {

        return null;
    }

    /**
     *  removes the USer with the specified id
     * @param id
     *      id must be not null
     * @return an {@code Optional}
     *            - null if there is no entity with the given id,
     *            - the removed entity, otherwise
     */
    public Optional<User> remove(Long id){
        return repo.delete(id);
    }

    /**
     * Method for obtaining a user with a particular ID
     * @param id -the id of the entity to be returned
     * @return an {@code Optional} encapsulating the entity with the given id
     */
    public Optional<User> findOne(Long id){
        return this.repo.findOne(id);
    }

    public Optional<User> findUserByUserName(String userName){
        Iterable<User> users = repo.findAll();
        return StreamSupport.stream(users.spliterator(),false)
                .filter(user -> user.getUserName().equals(userName))
                .findFirst();
    }

    @Override
    public List<User> getEntities(int page){
        Pageable pageable = new PageableImplementation(page,pageSize);
        Page<User> all = repo.findAll(pageable);
        return all.getContent().collect(Collectors.toList());
    }
}
