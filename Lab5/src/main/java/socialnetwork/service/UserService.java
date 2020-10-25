package socialnetwork.service;

import socialnetwork.domain.User;
import socialnetwork.repository.Repository;



import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserService {
    private Repository<Long, User> repo;

    public UserService(Repository<Long, User> repo) {
        this.repo = repo;
    }

    /**
     * Method for adding a user
     * @param user : User
     * @return an {@code Optional} - null if the entity was saved,
     *                             - the entity (id already exists)
     */
    public Optional<User> addUser(User user) {
        return repo.save(user);
    }

    public List<User> getAllUsers() {
        //este echivalent cu copiatul tuturor studentilor intr-o lista
        Iterable<User> students = repo.findAll();
        return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toList());
    }

    public List<User> filterUsersName(String s) {

        return null;
    }

    /**
     * removes the entity with the specified id
     * @param id : Long
     * @return an {@code Optional}
     *                  - null if there is no entity with the given id,
     *                 - the removed entity, otherwise
     */
    public Optional<User> removeUser(Long id){
        return repo.delete(id);
    }
}
