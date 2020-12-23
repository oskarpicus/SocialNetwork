package socialnetwork.repository.paging;

public class PageableImplementation implements Pageable{

    private final int pageNumber;
    private final int pageSize;

    public PageableImplementation(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }
}
