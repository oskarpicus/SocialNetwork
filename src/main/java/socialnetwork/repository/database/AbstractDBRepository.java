package socialnetwork.repository.database;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import java.sql.*;
import java.util.*;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    protected Map<ID,E> allEntities = null;
    String dataBaseName;
    protected Connection c;
    private final Validator<E> validator;

    protected AbstractDBRepository(Validator<E> validator,String dataBaseName) {
        this.validator = validator;
        this.dataBaseName = dataBaseName;
        connectToDataBase();
        loadFromDataBase();
    }

    private void loadFromDataBase(){
        allEntities = new HashMap<>();
        try{
            Statement statement = c.createStatement();
            String command = getFindAllCommand();
            ResultSet resultSet = statement.executeQuery(command);
            while(resultSet.next()){
                E entity = extractEntityFromResultSet(resultSet);
                allEntities.put(entity.getId(),entity);
            }
            statement.close();
            resultSet.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method for connecting to a data base
     */
    private void connectToDataBase(){
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/"+ dataBaseName,
                            "postgres", "florenta28");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<E> findOne(ID id) {
        if(id==null)
            throw new IllegalArgumentException("id must not be null");

        E entity;
        if((entity = this.allEntities.get(id))!=null){ // if it's in memory
            return Optional.of(entity);
        }
        // if it's in the data base
        try{
            Statement statement = c.createStatement();
            String command = getFindOneCommand(id);
            ResultSet resultSet = statement.executeQuery(command);

            if(!resultSet.next()){ // if the result set is empty
                return Optional.empty();
            }

            entity = extractEntityFromResultSet(resultSet);
            statement.close();
            resultSet.close();
            return Optional.of(entity);
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Iterable<E> findAll() {
        return allEntities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must not be null");
        validator.validate(entity);

        try{
            Statement statement = c.createStatement();
            String command = getSaveCommand(entity);
            statement.executeUpdate(command);
            statement.close();
            allEntities.put(entity.getId(),entity);
            return Optional.empty();
        }catch (Exception e){
            e.printStackTrace();
            return Optional.of(entity);
        }
    }


    @Override
    public Optional<E> delete(ID id) {
        if(id==null)
            throw new IllegalArgumentException("id must not be null");

        try{
            Statement statement = c.createStatement();
            Optional<E> entity = findOne(id);
            if(entity.isEmpty()){ // if the entity does not exist
                return Optional.empty();
            }

            String command = getDeleteCommand(id);
            statement.executeUpdate(command);
            statement.close();
            allEntities.remove(id);
            return entity;
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<E> update(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must not be null");
        validator.validate(entity);

        try{
            Statement statement = c.createStatement();
            String command = getUpdateCommand(entity);
            statement.executeUpdate(command);
            statement.close();
            return Optional.empty();
        }catch (Exception e){
            return Optional.of(entity);
        }
    }

    /**
     * Method for obtaining the SQL Command, in order to save an entity
     * @param entity : E , entity to be saved
     * @return command : String, if it's executed it will save entity in the data base
     */
    protected abstract String getSaveCommand(E entity);

    /**
     * Method for obtaining the SQL Command, in order to delete an entity
     * @param id : ID , id of the entity to be deleted
     * @return command : String, if it's executed it will delete the entity with id from the data base
     */
    protected abstract String getDeleteCommand(ID id);

    /**
     * Method for obtaining the SQL Command, in order to find an entity
     * @param id : ID , id of the entity to find
     * @return command : String, if it's executed it will select the entity with id from the data base
     */
    protected abstract String getFindOneCommand(ID id);

    /**
     * Method for obtaining the SQL Command, in order to get all of the saved entities
     * @return command : String, if it's executed it will select all entities from the data base
     */
    protected abstract String getFindAllCommand();

    /**
     * Extract the entity from a result set
     * @param resultSet : ResultSet, it is not empty
     * @return entity : E, composed of the data from resultSet
     */
    protected abstract E extractEntityFromResultSet(ResultSet resultSet);

    /**
     * Method for obtaining the SQL Command, in order to update the entity
     * @param entity : the updated entity
     * @return command : String, it it's executed it will update the entity in the data base
     */
    protected abstract String getUpdateCommand(E entity);

}
