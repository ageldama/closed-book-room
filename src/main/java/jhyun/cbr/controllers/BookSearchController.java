package jhyun.cbr.controllers;

import io.swagger.annotations.*;
import jhyun.cbr.controllers.book_search.BookSearchResultWithSavedHistory;
import jhyun.cbr.controllers.http_status_exceptions.NoSuchBookSearchProviderException;
import jhyun.cbr.rest_resources.book_search.BookSearchRequest;
import jhyun.cbr.rest_resources.book_search.BookSearchResult;
import jhyun.cbr.services.BookSearchHistoryService;
import jhyun.cbr.services.BookSearchProviders;
import jhyun.cbr.services.BookSearchService;
import jhyun.cbr.storage.entities.BookSearchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api
@RequestMapping("/v1/book")
@RestController
public class BookSearchController {
    private BookSearchProviders bookSearchProviders;
    private BookSearchHistoryService bookSearchHistoryService;
    private SpringSecurityHelper springSecurityHelper;

    @Autowired
    public BookSearchController(BookSearchProviders bookSearchProviders,
                                BookSearchHistoryService bookSearchHistoryService,
                                SpringSecurityHelper springSecurityHelper) {
        //
        this.bookSearchProviders = bookSearchProviders;
        this.bookSearchHistoryService = bookSearchHistoryService;
        this.springSecurityHelper = springSecurityHelper;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "로그인 필요"),
    })
    @ApiOperation(value = "책 검색")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/search/{providerId}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BookSearchResultWithSavedHistory search(
            @ApiParam(value = "책검색API 제공자ID", required = true, example = "kakao",
                    examples = @Example({
                            @ExampleProperty(value = "kakao"),
                    }))
            @PathVariable("providerId")
                    String providerId,
            @ApiParam(required = true,
                    value = "책검색 검색쿼리. 검색API제공자에 따라 다른 `BookSearchRequest`의 구현 클래스의 내용을 JSON으로 요청")
            @RequestBody String searchRequestStr) {
        //
        final String username = springSecurityHelper.getUsernameOrThrow();
        //
        final Optional<BookSearchService> bookSearchProviderOptional =
                bookSearchProviders.getBookSearchService(providerId);
        if (!bookSearchProviderOptional.isPresent()) {
            throw new NoSuchBookSearchProviderException(providerId);
        }
        //
        final BookSearchService bookSearchService = bookSearchProviderOptional.get();
        final BookSearchRequest searchRequest = bookSearchService.parseJsonAsRequest(searchRequestStr);
        //
        final BookSearchResult searchResult = bookSearchService.search(searchRequest);
        final BookSearchHistory savedHistory = bookSearchHistoryService.saveNewHistory(
                username, providerId, searchRequest);
        return new BookSearchResultWithSavedHistory(searchResult, savedHistory.getId());
    }

}
