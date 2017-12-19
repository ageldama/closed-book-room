package jhyun.cbr.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import jhyun.cbr.rest_resources.book_search.BookSearchRequest;
import jhyun.cbr.services.exceptions.BookSearchHistoryBookmarkExistingException;
import jhyun.cbr.storage.entities.BookSearchHistory;
import jhyun.cbr.storage.entities.BookSearchHistoryBookmarkMap;
import jhyun.cbr.storage.repositories.BookSearchHistoryBookmarkMapRepository;
import jhyun.cbr.storage.repositories.BookSearchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Verify.verify;

@Service
public class BookSearchHistoryService {

    private ObjectMapper objectMapper;
    private BookSearchHistoryRepository bookSearchHistoryRepository;
    private BookSearchHistoryBookmarkMapRepository bookSearchHistoryBookmarkMapRepository;
    private BookSearchProviders bookSearchProviders;

    @Autowired
    public BookSearchHistoryService(
            ObjectMapper objectMapper,
            BookSearchHistoryRepository bookSearchHistoryRepository,
            BookSearchHistoryBookmarkMapRepository bookSearchHistoryBookmarkMapRepository,
            BookSearchProviders bookSearchProviders
    ) {
        this.objectMapper = objectMapper;
        this.bookSearchHistoryRepository = bookSearchHistoryRepository;
        this.bookSearchHistoryBookmarkMapRepository = bookSearchHistoryBookmarkMapRepository;
        this.bookSearchProviders = bookSearchProviders;
    }

    @Transactional
    public BookSearchHistory saveNewHistory(
            final String username, final String providerId, final BookSearchRequest request
    ) {
        checkArgument(!Strings.isNullOrEmpty(username));
        checkArgument(!Strings.isNullOrEmpty(providerId));
        checkArgument(bookSearchProviders.getProviderIds().contains(providerId));
        checkArgument(request != null);
        //
        String requestJson = null; // NOTE: not-final
        try {
            requestJson = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        final BookSearchHistory history = new BookSearchHistory(username, providerId, requestJson, false);
        return bookSearchHistoryRepository.save(history);
    }

    @Transactional
    public Optional<BookSearchHistory> copyAsBookmark(final Long id) throws BookSearchHistoryBookmarkExistingException {
        checkArgument(id != null);
        //
        final BookSearchHistory history = bookSearchHistoryRepository.findOne(id);
        if (history == null) return Optional.empty();
        else {
            if (0 < bookSearchHistoryBookmarkMapRepository.countByHistoryId(id)) {
                throw new BookSearchHistoryBookmarkExistingException(id);
            }
            //
            final BookSearchHistory bookmark =
                    new BookSearchHistory(history.getUsername(),
                            history.getProviderId(), history.getRequestJson(), true);
            final BookSearchHistory savedBookmark = bookSearchHistoryRepository.save(bookmark);
            verify(savedBookmark != null);
            //
            bookSearchHistoryBookmarkMapRepository.save(new BookSearchHistoryBookmarkMap(id, savedBookmark.getId()));
            //
            return Optional.of(savedBookmark);
        }
    }

    public Page<BookSearchHistory> list(final String username, final boolean bookmark,
                                        final Set<String> providerIds, final Pageable pageable) {
        checkArgument(!Strings.isNullOrEmpty(username));
        checkArgument(providerIds != null);
        checkArgument(!providerIds.isEmpty());
        checkArgument(bookSearchProviders.getProviderIds().containsAll(providerIds));
        checkArgument(pageable != null);
        //
        return bookSearchHistoryRepository.findByUsernameAndBookmarkAndProviderIdIn(
                username, bookmark, providerIds, pageable);
    }

    public Optional<BookSearchHistory> getById(final Long id) {
        checkArgument(id != null);
        //
        return Optional.ofNullable(bookSearchHistoryRepository.findOne(id));
    }

    @Transactional
    public void deleteById(final Long id) {
        checkArgument(id != null);
        //
        final Optional<BookSearchHistory> it = this.getById(id);
        if (it.isPresent()) {
            final boolean isBookmark = it.get().getBookmark();
            bookSearchHistoryRepository.delete(id);
            if (isBookmark) {
                bookSearchHistoryBookmarkMapRepository.deleteByBookmarkId(id);
            }
        }
    }
}
