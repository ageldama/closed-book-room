package jhyun.cbr.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchRequest;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchResult;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static jhyun.cbr.testing_supports.TestingFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KakaoBookSearchServiceSearchTests {
    private final String restApiKey = "FOOBAR";

    private RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    private KakaoRequestToQueryStringService requestToQueryStringService =
            new KakaoRequestToQueryStringService();

    private KakaoBookSearchService testSubject = new KakaoBookSearchService(
            restApiKey, restTemplate, requestToQueryStringService, objectMapper);

    @Test
    public void searchOkNotEmpty() {
        // record mocks
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(Class.class)))
                .then(invocation -> new ResponseEntity<>(sampleKakaoBookSearchResultNotEmptyStr(), HttpStatus.OK));
        //
        final KakaoBookSearchResult result = testSubject.search(sampleRequest());
        // verify result
        assertThat(result).isNotNull();
        assertThat(result.getMeta()).isNotNull();
        assertThat(result.getDocuments()).isNotNull();
        // verify mocks
        verify(restTemplate, times(1))
                .exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(Class.class));
    }

    @Test
    public void searchOkButEmpty() {
        // record mocks
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(Class.class)))
                .then(invocation -> new ResponseEntity<>(sampleKakaoBookSearchResultButEmptyStr(), HttpStatus.OK));
        //
        final KakaoBookSearchResult result = testSubject.search(sampleRequest());
        // verify result
        assertThat(result).isNotNull();
        assertThat(result.getMeta()).isNotNull();
        assertThat(result.getDocuments()).isNotNull().isEmpty();    // YES, it is empty.
        // verify mocks
        verify(restTemplate, times(1))
                .exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(Class.class));
    }

    @Test
    public void categories() {
        assertThat(testSubject.categories()).isNotEmpty();
    }

    @Test
    public void providerId() {
        assertThat(testSubject.getProviderId()).isNotEmpty();
    }

    @Test
    public void parseJsonAsRequest() {
        final KakaoBookSearchRequest it = testSubject.parseJsonAsRequest("{}");
        assertThat(it).isNotNull().isInstanceOf(KakaoBookSearchRequest.class);
    }
}
