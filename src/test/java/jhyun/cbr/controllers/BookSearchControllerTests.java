package jhyun.cbr.controllers;

import jhyun.cbr.controllers.book_search.BookSearchResultWithSavedHistory;
import jhyun.cbr.controllers.http_status_exceptions.NoSuchBookSearchProviderException;
import jhyun.cbr.controllers.http_status_exceptions.NotAuthorizedException;
import jhyun.cbr.rest_resources.book_search.BookSearchRequest;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoConsts;
import jhyun.cbr.services.BookSearchHistoryService;
import jhyun.cbr.services.BookSearchProviders;
import jhyun.cbr.services.BookSearchService;
import jhyun.cbr.storage.entities.BookSearchHistory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static jhyun.cbr.controllers.SpringSecurityHelperMocks.recordSpringSecurityHelper_noSuchUser;
import static jhyun.cbr.controllers.SpringSecurityHelperMocks.recordSpringSecurityHelper_ok;
import static jhyun.cbr.services.BookSearchProvidersMocks.recordBookSearchProviders_getBookSearchService_empty;
import static jhyun.cbr.services.BookSearchProvidersMocks.recordBookSearchProviders_getBookSearchService_ok;
import static jhyun.cbr.testing_supports.TestingFixtures.emptyBookSearchRequestStr;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookSearchControllerTests {
    @Mock
    private BookSearchProviders bookSearchProviders;

    @Spy
    private SpringSecurityHelper springSecurityHelper;

    @Mock
    private BookSearchHistoryService bookSearchHistoryService;

    @InjectMocks
    private BookSearchController testSubject;


    private void recordBookSearchHistoryService_saveNewHistory() {
        when(bookSearchHistoryService.saveNewHistory(
                anyString(), anyString(), any(BookSearchRequest.class)))
                .then(invocation-> {
                    final String username = Objects.toString(invocation.getArguments()[0]);
                    final String providerId = Objects.toString(invocation.getArguments()[1]);
                    final BookSearchHistory it = new BookSearchHistory();
                    it.setUsername(username);
                    it.setCtime(new Date());
                    it.setRequestJson("{}");
                    it.setProviderId(providerId);
                    it.setBookmark(false);
                    it.setId(1234L);
                    return it;
                });
    }

    // `search`
    @Test
    public void search_notLoggedIn() {
        recordSpringSecurityHelper_noSuchUser(springSecurityHelper);
        recordBookSearchProviders_getBookSearchService_empty(bookSearchProviders);
        //
        assertThatThrownBy(() -> testSubject.search(KakaoConsts.PROVIDER_ID, emptyBookSearchRequestStr()))
                .isInstanceOf(NotAuthorizedException.class);
        //
        verify(bookSearchProviders, times(0))
                .getBookSearchService(anyString());
    }

    @Test
    public void search_noSuchBookSearchProvider() {
        recordSpringSecurityHelper_ok(springSecurityHelper);
        recordBookSearchProviders_getBookSearchService_empty(bookSearchProviders);
        //
        assertThatThrownBy(() -> testSubject.search("Fussbar-GmbH", emptyBookSearchRequestStr()))
                .isInstanceOf(NoSuchBookSearchProviderException.class);
    }

    @Test
    public void search_ok() throws InterruptedException, ExecutionException, TimeoutException {
        final CompletableFuture<String> usernameFuture = recordSpringSecurityHelper_ok(springSecurityHelper);
        final CompletableFuture<BookSearchService> mockSearchServiceFuture =
                recordBookSearchProviders_getBookSearchService_ok(bookSearchProviders);
        recordBookSearchHistoryService_saveNewHistory();
        //
        final BookSearchResultWithSavedHistory response = testSubject.search(KakaoConsts.PROVIDER_ID, emptyBookSearchRequestStr());
        assertThat(response).isNotNull();
        assertThat(response.getHistoryId()).isNotNull();
        assertThat(response.getBookSearchResult()).isNotNull();
        //
        final BookSearchService mockSearchService = mockSearchServiceFuture.get(10, TimeUnit.MILLISECONDS);
        verify(mockSearchService, times(1))
                .search(any(BookSearchRequest.class));
        //
        final String username = usernameFuture.get(10, TimeUnit.MILLISECONDS);
        verify(bookSearchHistoryService, times(1))
                .saveNewHistory(eq(username), eq(KakaoConsts.PROVIDER_ID), any(BookSearchRequest.class));
    }
}