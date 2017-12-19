package jhyun.cbr.storage.repositories;

import jhyun.cbr.storage.entities.BookSearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

/**
 * 도서 검색 히스토리/북마크 저장소
 */
public interface BookSearchHistoryRepository extends PagingAndSortingRepository<BookSearchHistory, Long> {

    Page<BookSearchHistory> findByUsernameAndBookmarkAndProviderIdIn(
            String username, boolean bookmark, Collection<String> providerIds, Pageable pageable);

}
