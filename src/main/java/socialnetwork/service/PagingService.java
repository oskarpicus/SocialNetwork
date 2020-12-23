package socialnetwork.service;

import socialnetwork.domain.Entity;

import java.util.List;

public interface PagingService<ID,E extends Entity<ID>> extends Service<ID,E> {

    int pageSize = 3;

    List<E> getEntities(int page);

}
