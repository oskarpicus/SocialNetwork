package socialnetwork.repository.paging;

import java.util.stream.Stream;

public class PageImplementation<E> implements Page<E> {

    private final Pageable pageable;
    private final Stream<E> content;

    public PageImplementation(Pageable pageable, Stream<E> content) {
        this.pageable = pageable;
        this.content = content;
    }

    @Override
    public Pageable getPageable() {
        return pageable;
    }

    @Override
    public Pageable nextPageable() {
        return new PageableImplementation(this.pageable.getPageNumber()+1,this.pageable.getPageSize());
    }

    @Override
    public Pageable previousPageable() {
        if(pageable.getPageNumber()==0){ //there is no previous page
            return pageable;
        }
        return new PageableImplementation(this.pageable.getPageNumber()-1,this.pageable.getPageSize());
    }

    @Override
    public Stream<E> getContent() {
        return content;
    }
}
