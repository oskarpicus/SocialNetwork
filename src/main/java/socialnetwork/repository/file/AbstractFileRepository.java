package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


///Aceasta clasa implementeaza sablonul de proiectare Template Method; puteti inlucui solutia propusa cu un Factori (vezi mai jos)
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
        loadData();

    }

    private void loadData() {
        //sau cu lambda - curs 4, sem 4 si 5
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(linie -> {
                E entity=extractEntity(Arrays.asList(linie.split(";")));
                super.save(entity);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes : List<String>
     * @return an entity of type E
     */
    //face obiecte din string-urile citite din fisier
    public abstract E extractEntity(List<String> attributes);
    ///Observatie-Sugestie: in locul metodei template extractEntity, puteti avea un factory pr crearea instantelor entity

    /**
     * Create entity as String
     * @param entity : E
     * @return String representation of entity
     */
    protected abstract String createEntityAsString(E entity);

    @Override
    public Optional<E> save(E entity){
        Optional<E> result = super.save(entity);
        if(result.equals(Optional.empty()))
            writeToFile(entity);
        return result;
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> result = super.delete(id);
        if(!result.equals(Optional.empty()))
            writeAllToFile();
        return result;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> result = super.update(entity);
        if(result.equals(Optional.empty()))
            writeAllToFile();
        return result;
    }


    protected void writeToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void writeAllToFile(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))){ // it automatically clears the file
            Iterable<E> listAll = super.findAll();
            listAll.forEach(entity ->{
                try {
                    bw.write(createEntityAsString(entity));
                    bw.newLine();
                }catch (IOException e ){
                    e.printStackTrace();
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}

