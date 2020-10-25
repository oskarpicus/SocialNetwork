package socialnetwork.repository.memory;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;



import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        if(id==null)
            throw new IllegalArgumentException("id must not be null");
        return Optional.ofNullable(this.entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity)  {
        if (entity==null)
            throw new IllegalArgumentException("entity must not be null");
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(),entity));
    }

    @Override
    public Optional<E> delete(ID id) {
        if(id==null)
            throw new IllegalArgumentException("id must not be null");
        return Optional.ofNullable(this.entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must not be null");
        validator.validate(entity);
        if(entities.replace(entity.getId(),entity)!=null) //there was a previous value
            return Optional.empty();
        return Optional.of(entity);
    }

}
