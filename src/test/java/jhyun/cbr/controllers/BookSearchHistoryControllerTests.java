package jhyun.cbr.controllers;

import jhyun.cbr.controllers.http_status_exceptions.NotAuthorizedException;
import jhyun.cbr.services.BookSearchHistoryService;
import jhyun.cbr.services.BookSearchProviders;
import jhyun.cbr.storage.entities.BookSearchHistory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static jhyun.cbr.controllers.SpringSecurityHelperMocks.recordSpringSecurityHelper_noSuchUser;
import static jhyun.cbr.controllers.SpringSecurityHelperMocks.recordSpringSecurityHelper_ok;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookSearchHistoryControllerTests {

    @Spy
    private SpringSecurityHelper springSecurityHelper;

    @Mock
    private BookSearchProviders bookSearchProviders;

    @Mock
    private BookSearchHistoryService bookSearchHistoryService;

    @InjectMocks
    private BookSearchHistoryController testSubject;

    private void recordBookSearchHistoryService_list_ok() {
        when(bookSearchHistoryService
                .list(anyString(), anyBoolean(), notNull(Set.class), notNull(Pageable.class)))
                .then(invocationOnMock ->
                        new PageImpl<BookSearchHistory>(Collections.emptyList()));
    }

    // `list`
    @Test
    public void list_ok() throws InterruptedException, ExecutionException, TimeoutException {
        final CompletableFuture<String> usernameFuture =
                recordSpringSecurityHelper_ok(springSecurityHelper);
        recordBookSearchHistoryService_list_ok();
        //
        final List<BookSearchHistory> response = testSubject.list(0, 1);
        assertThat(response).isNotNull();
        //
        final String username = usernameFuture.get(10, TimeUnit.MILLISECONDS);
        verify(bookSearchHistoryService, times(1))
                .list(eq(username), eq(false), notNull(Set.class), notNull(Pageable.class));
    }

    @Test
    public void list_notLoggedIn() {
        recordSpringSecurityHelper_noSuchUser(springSecurityHelper);
        recordBookSearchHistoryService_list_ok();
        //
        assertThatThrownBy(() -> testSubject.list(0, 1))
                .isInstanceOf(NotAuthorizedException.class);
        //
        verify(bookSearchHistoryService, times(0))
                .list(anyString(), anyBoolean(), notNull(Set.class), notNull(Pageable.class));
    }
}