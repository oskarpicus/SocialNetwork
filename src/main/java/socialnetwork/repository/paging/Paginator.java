package socialnetwork.repository.paging;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Paginator<E> {
    private final Pageable pageable;
    private final Iterable<E> content;

    public Paginator(Pageable pageable, Iterable<E> content) {
        this.pageable = pageable;
        this.content = content;
    }

    public Page<E> paginate(){
        Stream<E> result= StreamSupport.stream(content.spliterator(), false)
                .skip(pageable.getPageNumber()  * pageable.getPageSize())
                .limit(pageable.getPageSize());
        return new PageImplementation<>(pageable, result);
    }
}
