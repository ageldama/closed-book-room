package jhyun.cbr.services;

import jhyun.cbr.rest_resources.book_search.BookSearchRequest;
import jhyun.cbr.rest_resources.book_search.BookSearchResult;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class BookSearchProvidersMocks {
    private static OngoingStubbing<BookSearchResult> recordEmptyResultOfSearch(BookSearchService it, String providerId) {
        return when(it.search(any(BookSearchRequest.class)))
                .then(invocation -> new BookSearchResult() {
                    @Override
                    public String getProviderId() {
                        return providerId;
                    }
                });
    }

    public static CompletableFuture<BookSearchService> recordBookSearchProviders_getBookSearchService_ok(
            BookSearchProviders bookSearchProviders
    ) {
        final CompletableFuture<BookSearchService> bookSearchServiceFuture =
                new CompletableFuture<>();
        when(bookSearchProviders.getBookSearchService(anyString()))
                .then(invocation -> {
                    final String providerId = Objects.toString(invocation.getArguments()[0]);
                    final BookSearchService it = mock(BookSearchService.class);
                    recordEmptyResultOfSearch(it, providerId);
                    bookSearchServiceFuture.complete(it);
                    return Optional.of(it);
                });
        return bookSearchServiceFuture;
    }

    public static void recordBookSearchProviders_getBookSearchService_empty(
            BookSearchProviders bookSearchProviders
    ) {
        when(bookSearchProviders.getBookSearchService(anyString()))
                .thenReturn(Optional.empty());
    }
}
