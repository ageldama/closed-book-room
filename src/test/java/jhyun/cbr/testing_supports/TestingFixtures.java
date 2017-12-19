package jhyun.cbr.testing_supports;

import com.fasterxml.jackson.databind.ObjectMapper;
import jhyun.cbr.rest_resources.book_search.BookSearchRequest;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchSortOrders;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchTargets;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchRequest;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchResult;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public final class TestingFixtures {

    public static KakaoBookSearchRequest sampleRequest() {
        final String query = "QUERY한글";
        final KakaoBookSearchSortOrders sort = KakaoBookSearchSortOrders.recency;
        final Integer page = 31;
        final Integer size = 42;
        final KakaoBookSearchTargets target = KakaoBookSearchTargets.publisher;
        final Integer category = 953;
        return new KakaoBookSearchRequest(query, sort, page, size, target, category);
    }

    private static String readResourceAsUtf8String(final String name) {
        final String contents;
        try {
            contents = IOUtils.resourceToString(
                    name, com.google.common.base.Charsets.UTF_8);
            return contents;
        } catch (IOException e) {
            throw new RuntimeException("IOUtils.resourceToString FAIL", e);
        }
    }

    private static KakaoBookSearchResult sampleKakaoBookSearchResult(final String name) throws IOException {
        final String contents = readResourceAsUtf8String(name);
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(contents, KakaoBookSearchResult.class);
    }

    public static String sampleKakaoBookSearchResultNotEmptyStr() {
        return readResourceAsUtf8String("/samples/search-result-ok.json");
    }

    public static String sampleKakaoBookSearchResultButEmptyStr() {
        return readResourceAsUtf8String("/samples/search-result-empty.json");
    }

    public static KakaoBookSearchResult sampleKakaoBookSearchResultNotEmpty() throws IOException {
        return sampleKakaoBookSearchResult("/samples/search-result-ok.json");
    }

    public static KakaoBookSearchResult sampleKakaoBookSearchResultButEmpty() throws IOException {
        return sampleKakaoBookSearchResult("/samples/search-result-empty.json");
    }

    public static BookSearchRequest emptyBookSearchRequest() {
        return new BookSearchRequest() {
        };
    }

    public static String emptyBookSearchRequestStr() {
        return "{}";
    }

}
