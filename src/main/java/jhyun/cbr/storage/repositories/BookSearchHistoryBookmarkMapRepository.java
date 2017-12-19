package jhyun.cbr.storage.repositories;

import jhyun.cbr.storage.entities.BookSearchHistoryBookmarkMap;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookSearchHistoryBookmarkMapRepository
        extends PagingAndSortingRepository<BookSearchHistoryBookmarkMap, Long> {

    int countByHistoryId(Long historyId);
    void deleteByBookmarkId(Long bookmarkId);
}
