package antessio.paymentsystem.common;

import java.util.List;

public class PaginatedList <T>{

    private List<T> data;

    private boolean hasMore;

    public PaginatedList(List<T> data, boolean hasMore) {
        this.data = data;
        this.hasMore = hasMore;
    }

    public List<T> getData() {
        return data;
    }

    public boolean isHasMore() {
        return hasMore;
    }

}
