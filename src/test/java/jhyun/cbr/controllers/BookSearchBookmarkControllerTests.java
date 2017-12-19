package jhyun.cbr.controllers;

import jhyun.cbr.controllers.http_status_exceptions.BookSearchHistoryBookmarkAlreadyExistingHttpStatusException;
import jhyun.cbr.controllers.http_status_exceptions.BookSearchHistoryNotFoundException;
import jhyun.cbr.controllers.http_status_exceptions.BookSearchHistoryOfAnotherUserException;
import jhyun.cbr.controllers.http_status_exceptions.NotAuthorizedException;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoConsts;
import jhyun.cbr.services.BookSearchHistoryService;
import jhyun.cbr.services.BookSearchProviders;
import jhyun.cbr.services.exceptions.BookSearchHistoryBookmarkExistingException;
import jhyun.cbr.storage.entities.BookSearchHistory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static jhyun.cbr.controllers.SpringSecurityHelperMocks.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookSearchBookmarkControllerTests {

    @Mock
    private BookSearchHistoryService bookSearchHistoryService;

    @Spy
    private SpringSecurityHelper springSecurityHelper;

    @Mock
    private BookSearchProviders bookSearchProviders;

    @InjectMocks
    private BookSearchBookmarkController testSubject;

    private void recordBookSearchHistoryService_deleteById() {
        doNothing().when(bookSearchHistoryService).deleteById(anyLong());
    }

    private void recordBookSearchHistoryService_getById_empty() {
        when(bookSearchHistoryService.getById(anyLong()))
                .thenReturn(Optional.empty());
    }

    private BookSearchHistory sampleBookSearchHistory_asBookmark(final Long id, final String username) {
        final BookSearchHistory it = new BookSearchHistory();
        it.setId(id);
        it.setBookmark(true);
        it.setProviderId(KakaoConsts.PROVIDER_ID);
        it.setRequestJson("{}");
        it.setCtime(new Date());
        it.setUsername(username);
        return it;
    }

    private void recordBookSearchHistoryService_getById_withUsername(final String username) {
        when(bookSearchHistoryService.getById(anyLong()))
                .then(invocationOnMock ->
                        Optional.of(sampleBookSearchHistory_asBookmark(
                                (Long) invocationOnMock.getArguments()[0], username)));
    }

    private void recordBookSearchHistoryService_copyAsBookmark(
            final String username
    ) throws BookSearchHistoryBookmarkExistingException {
        when(bookSearchHistoryService.copyAsBookmark(anyLong()))
                .then(invocationOnMock -> Optional.of(sampleBookSearchHistory_asBookmark(
                        (Long) invocationOnMock.getArguments()[0] * 2, username)));
    }

    private void recordBookSearchHistoryService_copyAsBookmark_throwsBookmarkExistingException() throws BookSearchHistoryBookmarkExistingException {
        when(bookSearchHistoryService.copyAsBookmark(anyLong()))
                .thenThrow(BookSearchHistoryBookmarkAlreadyExistingHttpStatusException.class);
    }

    private void recordBookSearchHistoryService_list() {
        when(bookSearchHistoryService.list(
                anyString(), anyBoolean(), any(Set.class), any(Pageable.class)))
                .then(invocation -> new PageImpl<BookSearchHistory>(Collections.emptyList()));
    }

    // `list`
    @Test
    public void list_ok() throws InterruptedException, ExecutionException, TimeoutException {
        final CompletableFuture<String> usernameFuture = recordSpringSecurityHelper_ok(springSecurityHelper);
        recordBookSearchHistoryService_list();
        //
        final Page<BookSearchHistory> response = testSubject.list(0, 1, "ctime", Sort.Direction.DESC);
        assertThat(response).isNotNull();
        //
        verify(bookSearchHistoryService, times(1))
                .list(eq(usernameFuture.get(10, TimeUnit.MILLISECONDS)),
                        eq(true), notNull(Set.class), notNull(Pageable.class));
    }

    @Test
    public void list_notLoggedIn() {
        recordSpringSecurityHelper_noSuchUser(springSecurityHelper);
        recordBookSearchHistoryService_list();
        //
        assertThatThrownBy(() ->
                testSubject.list(0, 1, "ctime", Sort.Direction.DESC))
                .isInstanceOf(NotAuthorizedException.class);
        //
        verify(bookSearchHistoryService, times(0))
                .list(anyString(), anyBoolean(), notNull(Set.class), notNull(Pageable.class));
    }

    // `copyHistoryAsBookmark`
    @Test
    public void copyHistoryAsBookmark_ok() throws BookSearchHistoryBookmarkExistingException {
        final String username = "foobar";
        recordSpringSecurityHelper_okWithStaticUsername(springSecurityHelper, username);
        recordBookSearchHistoryService_getById_withUsername(username);
        recordBookSearchHistoryService_copyAsBookmark(username);
        //
        final Long id = 1234L;
        final BookSearchHistory response = testSubject.copyHistoryAsBookmark(id);
        assertThat(response).isNotNull();
        //
        verify(bookSearchHistoryService, times(1)).copyAsBookmark(eq(id));
    }

    @Test
    public void copyHistoryAsBookmark_notLoggedIn() throws BookSearchHistoryBookmarkExistingException {
        recordSpringSecurityHelper_noSuchUser(springSecurityHelper);
        recordBookSearchHistoryService_copyAsBookmark("foobar?");
        //
        final Long id = 1234L;
        assertThatThrownBy(() -> testSubject.copyHistoryAsBookmark(id))
                .isInstanceOf(NotAuthorizedException.class);
        //
        verify(bookSearchHistoryService, times(0)).copyAsBookmark(anyLong());
    }

    @Test
    public void copyHistoryAsBookmark_historyOfAnotherUser() throws BookSearchHistoryBookmarkExistingException {
        final String username = "foobar";
        recordSpringSecurityHelper_okWithStaticUsername(springSecurityHelper, username);
        recordBookSearchHistoryService_getById_withUsername(username + username);
        recordBookSearchHistoryService_copyAsBookmark(username);
        //
        final Long id = 1234L;
        assertThatThrownBy(() -> testSubject.copyHistoryAsBookmark(id))
                .isInstanceOf(BookSearchHistoryOfAnotherUserException.class);
        //
        verify(bookSearchHistoryService, times(0)).copyAsBookmark(anyLong());
    }

    @Test
    public void copyHistoryAsBookmark_noSuchHistory() throws BookSearchHistoryBookmarkExistingException {
        final String username = "foobar";
        recordSpringSecurityHelper_okWithStaticUsername(springSecurityHelper, username);
        recordBookSearchHistoryService_getById_empty();
        recordBookSearchHistoryService_copyAsBookmark(username);
        //
        final Long id = 1234L;
        assertThatThrownBy(() -> testSubject.copyHistoryAsBookmark(id))
                .isInstanceOf(BookSearchHistoryNotFoundException.class);
        //
        verify(bookSearchHistoryService, times(0)).copyAsBookmark(anyLong());
    }

    @Test
    public void copyHistoryAsBookmark_bookmarkAlreadyExistingForHistory() throws BookSearchHistoryBookmarkExistingException {
        final String username = "foobar";
        recordSpringSecurityHelper_okWithStaticUsername(springSecurityHelper, username);
        recordBookSearchHistoryService_getById_withUsername(username);
        recordBookSearchHistoryService_copyAsBookmark_throwsBookmarkExistingException();
        //
        final Long id = 1234L;
        assertThatThrownBy(() -> testSubject.copyHistoryAsBookmark(id))
                .isInstanceOf(BookSearchHistoryBookmarkAlreadyExistingHttpStatusException.class);
        //
        verify(bookSearchHistoryService, times(1)).copyAsBookmark(eq(id));
    }

    // `deleteBookmark`
    @Test
    public void deleteBookmark_ok() {
        final String username = "foobar";
        recordSpringSecurityHelper_okWithStaticUsername(springSecurityHelper, username);
        recordBookSearchHistoryService_getById_withUsername(username);
        recordBookSearchHistoryService_deleteById();
        //
        final Long id = 1234L;
        testSubject.deleteBookmark(id);
        //
        verify(bookSearchHistoryService, times(1)).getById(eq(id));
        verify(bookSearchHistoryService, times(1)).deleteById(eq(id));
    }

    @Test
    public void deleteBookmark_notLoggedIn() {
        recordSpringSecurityHelper_noSuchUser(springSecurityHelper);
        recordBookSearchHistoryService_deleteById();
        //
        assertThatThrownBy(() -> testSubject.deleteBookmark(1234L))
                .isInstanceOf(NotAuthorizedException.class);
        //
        verify(bookSearchHistoryService, times(0)).deleteById(anyLong());
    }

    @Test
    public void deleteBookmark_noSuchBookmark() {
        recordSpringSecurityHelper_ok(springSecurityHelper);
        recordBookSearchHistoryService_getById_empty();
        recordBookSearchHistoryService_deleteById();
        //
        final Long id = 1234L;
        assertThatThrownBy(() -> testSubject.deleteBookmark(id))
                .isInstanceOf(BookSearchHistoryNotFoundException.class);
        //
        verify(bookSearchHistoryService, times(1)).getById(eq(id));
        verify(bookSearchHistoryService, times(0)).deleteById(anyLong());
    }

    @Test
    public void deleteBookmark_bookmarkOfAnotherUser() {
        final String username = "foobar";
        recordSpringSecurityHelper_okWithStaticUsername(springSecurityHelper, username);
        recordBookSearchHistoryService_getById_withUsername(username + username);
        recordBookSearchHistoryService_deleteById();
        //
        final Long id = 1234L;
        assertThatThrownBy(() -> testSubject.deleteBookmark(id))
                .isInstanceOf(BookSearchHistoryOfAnotherUserException.class);
        //
        verify(bookSearchHistoryService, times(1)).getById(eq(id));
        verify(bookSearchHistoryService, times(0)).deleteById(anyLong());
    }
}