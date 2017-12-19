package jhyun.cbr.services;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

@Scope("singleton")
@Component
public class BookSearchProviders {
    private KakaoBookSearchService kakaoBookSearchService;
    private Map<String, BookSearchService> bookSearchServices;

    @Autowired
    public BookSearchProviders(KakaoBookSearchService kakaoBookSearchService) {
        this.kakaoBookSearchService = kakaoBookSearchService;
        //
        bookSearchServices = ImmutableMap.of(
                KakaoConsts.PROVIDER_ID, kakaoBookSearchService
        );
    }

    public Set<String> getProviderIds() {
        return bookSearchServices.keySet();
    }

    public Optional<BookSearchService> getBookSearchService(final String providerId) {
        checkArgument(!Strings.isNullOrEmpty(providerId));
        return Optional.ofNullable(bookSearchServices.get(providerId));
    }
}
