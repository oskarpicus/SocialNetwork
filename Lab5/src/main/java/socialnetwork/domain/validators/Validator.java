package socialnetwork.domain.validators;

public interface Validator<T> {

    /**
     * Method for validating an object
     * @param entity: T
     * @throws ValidationException if entity is not valid
     */
    void validate(T entity) throws ValidationException;
}