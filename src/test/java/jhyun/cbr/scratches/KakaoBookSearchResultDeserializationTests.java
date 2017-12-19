package jhyun.cbr.scratches;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchEntry;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchMeta;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 도서 검색 결과 JSON을 Deserialization 테스트
 */
public class KakaoBookSearchResultDeserializationTests {

    @Ignore
    @Test
    public void loadSampleJsonAsStr() throws IOException {
        final String contents = readResourceAsUtf8String("/samples/search-result-ok.json");
        assertThat(contents).isNotEmpty();
    }

    private String readResourceAsUtf8String(final String name) {
        final String contents;
        try {
            contents = IOUtils.resourceToString(
                    name, com.google.common.base.Charsets.UTF_8);
            return contents;
        } catch (IOException e) {
            throw new RuntimeException("IOUtils.resourceToString FAIL", e);
        }
    }

    /** 검색결과 많을때 */
    @Test
    public void deserializeWithOkResult() throws IOException {
        final String contents = readResourceAsUtf8String("/samples/search-result-ok.json");
        assertThat(contents).isNotEmpty();
        //
        final ObjectMapper objectMapper = new ObjectMapper();
        final KakaoBookSearchResult result = objectMapper.readValue(contents, KakaoBookSearchResult.class);
        //
        assertThat(result).isNotNull();
        assertThat(result.getMeta()).isNotNull();
        assertThat(result.getDocuments()).isNotNull().isNotEmpty();
        //
        final KakaoBookSearchMeta meta = result.getMeta();
        assertThat(meta.getIsEnd()).isNotNull();
        assertThat(meta.getPageableCount()).isNotNegative();
        assertThat(meta.getTotalCount()).isNotNegative();
        //
        final List<KakaoBookSearchEntry> entries = result.getDocuments();
        for (int n = 0; n < entries.size(); n++) {
            assertSearchBookEntry(String.format("KakaoBookSearchEntry[%s-th]", n), entries.get(n));
        }
    }

    /** 검색결과 없을때 */
    @Test
    public void deserializeWithEmptyResult() throws IOException {
        final String contents = readResourceAsUtf8String("/samples/search-result-empty.json");
        assertThat(contents).isNotEmpty();
        //
        final ObjectMapper objectMapper = new ObjectMapper();
        final KakaoBookSearchResult result = objectMapper.readValue(contents, KakaoBookSearchResult.class);
        //
        assertThat(result).isNotNull();
        assertThat(result.getMeta()).isNotNull();
        assertThat(result.getDocuments()).isNotNull().isEmpty();
        //
        final KakaoBookSearchMeta meta = result.getMeta();
        assertThat(meta.getTotalCount()).isNotNull().isNotNegative();
        assertThat(meta.getPageableCount()).isNotNull().isNotNegative();
        assertThat(meta.getIsEnd()).isNotNull().isTrue();
    }

    private <T> Pair<String, T> named(final String name, T obj) {
        return new ImmutablePair<>(name, obj);
    }

    private void assertSearchBookEntry(final String name, final KakaoBookSearchEntry it) {
        ImmutableList.<Pair<String, Function<KakaoBookSearchEntry, ? extends Object>>>of(
                named("authors", KakaoBookSearchEntry::getAuthors),      // Can be empty
                named("barcode", KakaoBookSearchEntry::getBarcode),
                named("category", KakaoBookSearchEntry::getCategory),
                named("contents", KakaoBookSearchEntry::getContents),
                named("datetime", KakaoBookSearchEntry::getDatetime),
                named("ebookBarcode", KakaoBookSearchEntry::getEbookBarcode),
                named("isbn", KakaoBookSearchEntry::getIsbn),
                named("price", KakaoBookSearchEntry::getPrice),
                named("salePrice", KakaoBookSearchEntry::getSalePrice),
                named("publisher", KakaoBookSearchEntry::getPublisher),
                named("saleYn", KakaoBookSearchEntry::getSaleYn),
                named("status", KakaoBookSearchEntry::getStatus),
                named("thumbnail", KakaoBookSearchEntry::getThumbnail),
                named("title", KakaoBookSearchEntry::getTitle),
                named("translators", KakaoBookSearchEntry::getTranslators),  // Can be empty
                named("url", KakaoBookSearchEntry::getUrl)
        ).stream().forEach(getter -> {
            assertThat(getter.getValue().apply(it))
                    .as(name + "-" + getter.getKey())
                    .isNotNull();
        });
    }

}
