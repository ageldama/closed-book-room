package jhyun.cbr.storage.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchRequest;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchTargets;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class KakaoBookSearchRequestSerDesearTests {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testTarget() throws IOException {
        final KakaoBookSearchRequest req = new KakaoBookSearchRequest();
        req.setTarget(KakaoBookSearchTargets.title);
        final String jsonStr = objectMapper.writeValueAsString(req);
        final KakaoBookSearchRequest back = objectMapper.readValue(jsonStr, KakaoBookSearchRequest.class);
        assertThat(back).isNotNull();
        assertThat(back.getTarget()).isEqualTo(KakaoBookSearchTargets.title);
    }
}
