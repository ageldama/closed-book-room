package jhyun.cbr.services;

import com.google.common.base.Splitter;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchRequest;
import org.junit.Test;

import java.util.Map;

import static jhyun.cbr.testing_supports.TestingFixtures.sampleRequest;
import static org.assertj.core.api.Assertions.assertThat;

public class KakaoRequestToQueryStringServiceTests {

    private KakaoRequestToQueryStringService testSubject = new KakaoRequestToQueryStringService();

    @Test
    public void buildMap() {
        final KakaoBookSearchRequest r = sampleRequest();
        final Map<String, Object> m = testSubject.buildMap(r);
        //
        assertThat(m.get("query")).isEqualTo(r.getQuery());
        assertThat(m.get("sort")).isEqualTo(r.getSort().getValue());
        assertThat(m.get("page")).isEqualTo(r.getPage());
        assertThat(m.get("size")).isEqualTo(r.getSize());
        assertThat(m.get("target")).isEqualTo(r.getTarget().getValue());
        assertThat(m.get("category")).isEqualTo(r.getCategory());
    }

    @Test
    public void buildMapForNullQuery() {
        final KakaoBookSearchRequest r = new KakaoBookSearchRequest();
        r.setQuery(null);
        final Map<String, Object> m = testSubject.buildMap(r);
        assertThat(m).containsKeys("query");
        assertThat(m.get("query")).isEqualTo("");
    }

    @Test
    public void buildQueryString() {
        final KakaoBookSearchRequest r = sampleRequest();
        final String qs = testSubject.buildQueryString(r);
        //
        assertThat(qs).isNotBlank();
        final Iterable<String> parts = Splitter.on("&").split(qs);
        assertThat(parts).isNotNull().isNotEmpty();
        parts.forEach(s -> assertThat(s).isNotBlank());
    }
}
