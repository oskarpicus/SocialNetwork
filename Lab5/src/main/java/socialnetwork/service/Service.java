package socialnetwork.service;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.ValidationException;

import java.util.Optional;

public interface Service<ID,E extends Entity<ID>> {

    /**
     *
     * @param entity
     *         entity must be not null
     * @return an {@code Optional} - null if the entity was saved,
     *                             - the entity (id already exists)
     */
    Optional<E> add(E entity);


    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return an {@code Optional}
     *            - null if there is no entity with the given id,
     *            - the removed entity, otherwise
     */
    Optional<E> remove(ID id);

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    Optional<E> findOne(ID id);

}
