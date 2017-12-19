package jhyun.cbr.scratches;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Kakao 도서검색 API 스크래치.
 */
@RunWith(MockitoJUnitRunner.class)
public class BookSearchApiScratchTests {
	private RestTemplate restTemplate;

	@Before
	public void setUp() {
		restTemplate = new RestTemplate();
	}

	private String authorizationHeader() {
		final String AUTHORIZATION_HEADER_FMT = "KakaoAK %s";
		final String REST_API_KEY = "9b7471bbb7d0d1405fa11afbca9f4856";
		return String.format(AUTHORIZATION_HEADER_FMT, REST_API_KEY);
	}

	private <T> ResponseEntity<T> searchInTitle(final String keyword, final Class<T> responseClass) {
		final String url = String.format("https://dapi.kakao.com//v2/search/book?target=%s&query=%s", "title", keyword);
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authorizationHeader());
		final HttpEntity<String> request = new HttpEntity<>(headers);
		final ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, request, responseClass);
		return response;
	}

	@Test
	public void getResponseAsStrOk() throws IOException {
		final ResponseEntity<String> response = searchInTitle("한글", String.class);
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		final String bodyStr = response.getBody();
		assertThat(bodyStr).isNotNull();
		// FileUtils.write(new File("/tmp/foo.json"), bodyStr,
		// com.google.common.base.Charsets.UTF_8);
	}

}
