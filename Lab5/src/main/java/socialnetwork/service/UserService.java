package socialnetwork.service;

import socialnetwork.domain.User;
import socialnetwork.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserService implements Service<Long,User>{
    private final Repository<Long, User> repo;

    public UserService(Repository<Long, User> repo) {
        this.repo = repo;
    }

    public Optional<User> add(User user) {
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

    public Optional<User> remove(Long id){
        return repo.delete(id);
    }

    public Optional<User> findOne(Long id){
        return this.repo.findOne(id);
    }

}
