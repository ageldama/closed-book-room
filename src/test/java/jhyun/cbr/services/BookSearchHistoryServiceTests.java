package jhyun.cbr.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import jhyun.cbr.rest_resources.book_search.BookSearchRequest;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoConsts;
import jhyun.cbr.services.exceptions.BookSearchHistoryBookmarkExistingException;
import jhyun.cbr.storage.entities.BookSearchHistory;
import jhyun.cbr.storage.entities.BookSearchHistoryBookmarkMap;
import jhyun.cbr.storage.repositories.BookSearchHistoryBookmarkMapRepository;
import jhyun.cbr.storage.repositories.BookSearchHistoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static jhyun.cbr.testing_supports.TestingFixtures.emptyBookSearchRequest;
import static jhyun.cbr.testing_supports.ThrownByHelper.assertThrownByPairs;
import static jhyun.cbr.testing_supports.ThrownByHelper.thrownByPair;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookSearchHistoryServiceTests {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private BookSearchProviders bookSearchProviders;

    @Mock
    private BookSearchHistoryRepository bookSearchHistoryRepository;

    @Mock
    private BookSearchHistoryBookmarkMapRepository bookSearchHistoryBookmarkMapRepository;

    @InjectMocks
    private BookSearchHistoryService testSubject;

    private void recordBookSearchHistoryRepositry_findOne_alwaysReturnsNull() {
        when(bookSearchHistoryRepository.findOne(any(Long.class)))
                .thenReturn(null);
    }

    private void recordBookSearchHistoryRepositry_findOne_ok(boolean bookmark) {
        when(bookSearchHistoryRepository.findOne(any(Long.class)))
                .then(invocationOnMock -> {
                    final BookSearchHistory it = new BookSearchHistory();
                    it.setId((Long) invocationOnMock.getArguments()[0]);
                    it.setBookmark(bookmark);
                    it.setCtime(new Date());
                    it.setProviderId(KakaoConsts.PROVIDER_ID);
                    it.setRequestJson("");
                    it.setUsername("foobar-username");
                    return it;
                });
    }

    private void recordBookSearchHistoryRepository_delete() {
        doNothing().when(bookSearchHistoryRepository).delete(any(Long.class));
    }

    private void recordBookSearchHistoryBookmarkMapRepository_save() {
        when(bookSearchHistoryBookmarkMapRepository.save(any(BookSearchHistoryBookmarkMap.class)))
                .then(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }

    private void recordBookSearchHistoryRepository_save() {
        when(bookSearchHistoryRepository.save(any(BookSearchHistory.class)))
                .then(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }

    private void recordBookSearchProviders_getProviderIds() {
        when(bookSearchProviders.getProviderIds()).thenReturn(ImmutableSet.of(KakaoConsts.PROVIDER_ID));
    }

    // `saveNewHistory`
    @Test
    public void saveNewHistory_ok() throws Exception {
        // record mocks
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("{}");
        recordBookSearchHistoryRepository_save();
        recordBookSearchProviders_getProviderIds();
        //
        final String username = "foobar";
        final String providerId = "kakao";
        final BookSearchRequest request = emptyBookSearchRequest();
        final BookSearchHistory result = testSubject.saveNewHistory(username, providerId, request);
        assertThat(result).isNotNull();
        assertThat(result.getBookmark()).isFalse();
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getProviderId()).isEqualTo(providerId);
        assertThat(result.getCtime()).isNotNull();
        assertThat(result.getRequestJson()).isNotEmpty();
        //
        verify(bookSearchHistoryRepository, times(1))
                .save(eq(result));
    }

    @Test
    public void saveNewHistory_rejects() throws Exception {
        recordBookSearchProviders_getProviderIds();
        //
        final String username = "foobar";
        final String providerId = KakaoConsts.PROVIDER_ID;
        final BookSearchRequest request = emptyBookSearchRequest();
        assertThrownByPairs(
                thrownByPair("empty-username",
                        () -> testSubject.saveNewHistory("", providerId, request),
                        IllegalArgumentException.class),
                thrownByPair("null-username",
                        () -> testSubject.saveNewHistory(null, providerId, request),
                        IllegalArgumentException.class),
                thrownByPair("empty-providerId",
                        () -> testSubject.saveNewHistory(username, "", request),
                        IllegalArgumentException.class),
                thrownByPair("null-providerId",
                        () -> testSubject.saveNewHistory(username, null, request),
                        IllegalArgumentException.class),
                thrownByPair("unknown-providerId",
                        () -> testSubject.saveNewHistory(username, "Fussbar-GmbH", request),
                        IllegalArgumentException.class),
                thrownByPair("null-request",
                        () -> testSubject.saveNewHistory(username, providerId, null),
                        IllegalArgumentException.class)
        );
    }

    // `copyAsBookmark`
    @Test
    public void copyAsBookmark_ok() throws BookSearchHistoryBookmarkExistingException {
        recordBookSearchHistoryRepositry_findOne_ok(false);
        when(bookSearchHistoryBookmarkMapRepository.countByHistoryId(any(Long.class)))
                .thenReturn(0);
        recordBookSearchHistoryRepository_save();
        recordBookSearchHistoryBookmarkMapRepository_save();
        //
        final Long historyId = 1234L;
        final Optional<BookSearchHistory> savedBookmark = testSubject.copyAsBookmark(historyId);
        assertThat(savedBookmark.isPresent()).isTrue();
        assertThat(savedBookmark.get().getBookmark()).isTrue();
        // saved as new `BookSearchHistory`(bookmark=true)???
        final ArgumentCaptor<BookSearchHistory> savingBookmarkCaptor =
                ArgumentCaptor.forClass(BookSearchHistory.class);
        verify(bookSearchHistoryRepository, times(1))
                .save(savingBookmarkCaptor.capture());
        assertThat(savingBookmarkCaptor.getValue().getBookmark()).isTrue();
        // saved new bookmark-to-history mapping?
        final ArgumentCaptor<BookSearchHistoryBookmarkMap> savingBookmarkMapCaptor =
                ArgumentCaptor.forClass(BookSearchHistoryBookmarkMap.class);
        verify(bookSearchHistoryBookmarkMapRepository, times(1))
                .save(savingBookmarkMapCaptor.capture());
        assertThat(savingBookmarkMapCaptor.getValue().getHistoryId()).isEqualTo(historyId);
        //
        verify(bookSearchHistoryRepository, times(1))
                .findOne(eq(historyId));
        //
        verify(bookSearchHistoryBookmarkMapRepository, times(1))
                .countByHistoryId(eq(historyId));
    }

    @Test
    public void copyAsBookmark_rejectsNullId() {
        assertThatThrownBy(() -> {
            try {
                testSubject.copyAsBookmark(null);
            } catch (BookSearchHistoryBookmarkExistingException exc) {
                throw new RuntimeException(exc);
            }
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void copyAsBookmark_notFoundBaseHistory() throws BookSearchHistoryBookmarkExistingException {
        recordBookSearchHistoryRepositry_findOne_alwaysReturnsNull();
        //
        final Optional<BookSearchHistory> result = testSubject.copyAsBookmark(1234L);
        assertThat(result.isPresent()).isFalse();
        //
        verify(bookSearchHistoryRepository, times(1))
                .findOne(eq(1234L));
    }

    @Test
    public void copyAsBookmark_bookmarkAlreadyExists() {
        recordBookSearchHistoryRepositry_findOne_ok(false);
        when(bookSearchHistoryBookmarkMapRepository.countByHistoryId(any(Long.class)))
                .thenReturn(1);
        recordBookSearchHistoryRepository_save();
        recordBookSearchHistoryBookmarkMapRepository_save();
        //
        final Long historyId = 1234L;
        assertThatThrownBy(() -> testSubject.copyAsBookmark(historyId))
                .isInstanceOf(BookSearchHistoryBookmarkExistingException.class);
        // should not be called these methods
        verify(bookSearchHistoryRepository, times(0)).save(any(BookSearchHistory.class));
        verify(bookSearchHistoryBookmarkMapRepository, times(0))
                .save(any(BookSearchHistoryBookmarkMap.class));
        // but calls these to check the condition
        verify(bookSearchHistoryRepository, times(1))
                .findOne(eq(historyId));
        verify(bookSearchHistoryBookmarkMapRepository, times(1))
                .countByHistoryId(eq(historyId));
    }

    // `list`
    @Test
    public void list_rejects() {
        recordBookSearchProviders_getProviderIds();
        //
        final String username = "foobar";
        final Set<String> providerIds = ImmutableSet.of(KakaoConsts.PROVIDER_ID);
        final BookSearchRequest request = emptyBookSearchRequest();
        final Pageable pageable = new PageRequest(0, 1);
        assertThrownByPairs(
                thrownByPair("empty-username",
                        () -> testSubject.list("", false, providerIds, pageable),
                        IllegalArgumentException.class),
                thrownByPair("null-username",
                        () -> testSubject.list(null, false, providerIds, pageable),
                        IllegalArgumentException.class),
                thrownByPair("empty-providerIds",
                        () -> testSubject.list(username, false, ImmutableSet.of(), pageable),
                        IllegalArgumentException.class),
                thrownByPair("null-providerId",
                        () -> testSubject.list(username, false, null, pageable),
                        IllegalArgumentException.class),
                thrownByPair("unknown-providerId",
                        () -> testSubject.list(username, false, ImmutableSet.of("Fussbar-GmbH"), pageable),
                        IllegalArgumentException.class),
                thrownByPair("null-pageable",
                        () -> testSubject.list(username, false, providerIds, null),
                        IllegalArgumentException.class)
        );
    }

    @Test
    public void list_ok() {
        recordBookSearchProviders_getProviderIds();
        when(bookSearchHistoryRepository.findByUsernameAndBookmarkAndProviderIdIn(
                anyString(), any(Boolean.class), any(Collection.class), any(Pageable.class)))
                .then(inv -> new PageImpl<BookSearchHistory>(Collections.EMPTY_LIST));
        //
        final String username = "foobar";
        final Set<String> providerIds = ImmutableSet.of(KakaoConsts.PROVIDER_ID);
        final Pageable pageable = new PageRequest(0, 1);
        testSubject.list(username, false, providerIds, pageable);
        //
        verify(bookSearchHistoryRepository, times(1))
                .findByUsernameAndBookmarkAndProviderIdIn(
                        eq(username), eq(false), eq(providerIds), eq(pageable));
    }

    // `getById`
    @Test
    public void getById_rejectsNullId() {
        recordBookSearchHistoryRepositry_findOne_alwaysReturnsNull();
        assertThatThrownBy(() -> testSubject.getById(null))
                .isInstanceOf(IllegalArgumentException.class);
        verify(bookSearchHistoryRepository, times(0))
                .findOne(any(Long.class));
    }

    @Test
    public void getById_notFound() {
        recordBookSearchHistoryRepositry_findOne_alwaysReturnsNull();
        final Optional<BookSearchHistory> history = testSubject.getById(1234L);
        assertThat(history.isPresent()).isFalse();
        verify(bookSearchHistoryRepository, times(1))
                .findOne(eq(1234L));
    }

    @Test
    public void getById_ok() {
        recordBookSearchHistoryRepositry_findOne_ok(false);
        final Optional<BookSearchHistory> history = testSubject.getById(1234L);
        assertThat(history.isPresent()).isTrue();
        verify(bookSearchHistoryRepository, times(1))
                .findOne(eq(1234L));
    }

    // `deleteById`
    @Test
    public void deleteById_rejectsNullId() {
        recordBookSearchHistoryRepository_delete();
        assertThatThrownBy(() -> testSubject.deleteById(null))
                .isInstanceOf(IllegalArgumentException.class);
        verify(bookSearchHistoryRepository, times(0))
                .delete(any(Long.class));
    }

    @Test
    public void deleteById_okWithHistory() {
        recordBookSearchHistoryRepositry_findOne_ok(false);
        recordBookSearchHistoryRepository_delete();
        doNothing().when(bookSearchHistoryBookmarkMapRepository).deleteByBookmarkId(anyLong());
        //
        final Long id = 1234L;
        testSubject.deleteById(id);
        verify(bookSearchHistoryRepository, times(1))
                .delete(eq(id));
        verify(bookSearchHistoryBookmarkMapRepository, times(0))
                .deleteByBookmarkId(anyLong());
    }

    @Test
    public void deleteById_okWithBookmark() {
        recordBookSearchHistoryRepositry_findOne_ok(true);
        recordBookSearchHistoryRepository_delete();
        doNothing().when(bookSearchHistoryBookmarkMapRepository).deleteByBookmarkId(anyLong());
        //
        final Long id = 1234L;
        testSubject.deleteById(id);
        verify(bookSearchHistoryRepository, times(1))
                .delete(eq(id));
        verify(bookSearchHistoryBookmarkMapRepository, times(1))
                .deleteByBookmarkId(eq(id));
    }

}