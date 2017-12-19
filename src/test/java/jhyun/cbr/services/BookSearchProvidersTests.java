package jhyun.cbr.services;

import jhyun.cbr.rest_resources.book_search.kakao.KakaoConsts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static jhyun.cbr.testing_supports.ThrownByHelper.assertThrownByPairs;
import static jhyun.cbr.testing_supports.ThrownByHelper.thrownByPair;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BookSearchProvidersTests {
    @Mock
    private KakaoBookSearchService kakaoBookSearchService;

    @InjectMocks
    private BookSearchProviders testSubject;

    @Test
    public void getProviderIds() {
        assertThat(testSubject.getProviderIds()).isNotEmpty();
    }

    @Test
    public void getBookSearchService() throws Exception {
        assertThat(testSubject.getBookSearchService(KakaoConsts.PROVIDER_ID).isPresent()).isTrue();
        assertThrownByPairs(
                thrownByPair("null-id",
                        () -> testSubject.getBookSearchService(null),
                        IllegalArgumentException.class),
                thrownByPair("empty-id",
                        () -> testSubject.getBookSearchService(""),
                        IllegalArgumentException.class)
        );
    }

}