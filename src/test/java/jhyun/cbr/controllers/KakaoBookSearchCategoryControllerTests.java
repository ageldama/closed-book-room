package jhyun.cbr.controllers;

import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookCategory;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KakaoBookSearchCategoryControllerTests {

    private KakaoBookSearchCategoryController testSubject = new KakaoBookSearchCategoryController();

    @Test
    public void listCategories() throws Exception {
        final List<KakaoBookCategory> response = testSubject.listCategories();
        assertThat(response).isNotEmpty();
    }

}