package jhyun.cbr.services;

import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchRequest;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchResult;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchSortOrders;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchTargets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KakaoBookSearchServiceItTests {

    @Autowired
    private KakaoBookSearchService testSubject;

    @Test
    public void search() {
        final KakaoBookSearchResult result = testSubject.search(
                new KakaoBookSearchRequest("test", KakaoBookSearchSortOrders.recency, 1, 10,
                        KakaoBookSearchTargets.all, null));
        assertThat(result).isNotNull();
    }

}