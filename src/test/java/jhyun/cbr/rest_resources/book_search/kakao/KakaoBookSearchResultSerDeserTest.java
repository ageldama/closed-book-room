package jhyun.cbr.rest_resources.book_search.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static jhyun.cbr.testing_supports.TestingFixtures.sampleKakaoBookSearchResultNotEmpty;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * `KakaoBookSearchResult` JSON 변환 테스트.
 */
public class KakaoBookSearchResultSerDeserTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void serialization() throws IOException {
        final KakaoBookSearchResult result = sampleKakaoBookSearchResultNotEmpty();
        final String jsonStr = objectMapper.writeValueAsString(result);
        assertThat(jsonStr).contains("\"providerId\":\"kakao\"");
    }

    /**
     * `providerId`처럼 setter이 없는 경우에도 정상적으로 deserialization 가능한가?
     */
    @Test
    public void deserialization() throws IOException {
        final KakaoBookSearchResult result = sampleKakaoBookSearchResultNotEmpty();
        objectMapper.readValue(objectMapper.writeValueAsString(result), KakaoBookSearchResult.class);
    }
}