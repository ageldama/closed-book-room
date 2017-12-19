package jhyun.cbr.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookCategories;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookCategory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("카카오 책검색 API을 위한 코드표")
@RequestMapping("/v1/codes/kakao-book-search")
@RestController
public class KakaoBookSearchCategoryController {

    @ApiOperation("카테고리 코드표 리스팅")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
    })
    @RequestMapping(value = "/categories",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<KakaoBookCategory> listCategories() {
        return KakaoBookCategories.CATEGORIES;
    }
}
