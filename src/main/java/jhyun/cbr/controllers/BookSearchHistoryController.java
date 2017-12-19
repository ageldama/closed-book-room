package jhyun.cbr.controllers;

import io.swagger.annotations.*;
import jhyun.cbr.services.BookSearchHistoryService;
import jhyun.cbr.services.BookSearchProviders;
import jhyun.cbr.storage.entities.BookSearchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("책검색 히스토리")
@RequestMapping("/v1/book")
@RestController
public class BookSearchHistoryController {
    private BookSearchHistoryService bookSearchHistoryService;
    private BookSearchProviders bookSearchProviders;
    private SpringSecurityHelper springSecurityHelper;

    @Autowired
    public BookSearchHistoryController(
            BookSearchHistoryService bookSearchHistoryService,
            BookSearchProviders bookSearchProviders,
            SpringSecurityHelper springSecurityHelper
    ) {
        this.bookSearchHistoryService = bookSearchHistoryService;
        this.bookSearchProviders = bookSearchProviders;
        this.springSecurityHelper = springSecurityHelper;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "로그인 필요"),
    })
    @ApiOperation("검색 히스토리 리스팅")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/search-history", method = RequestMethod.GET)
    public List<BookSearchHistory> list(
            @ApiParam(required = false, defaultValue = "0", value = "페이지(0부터 시작)")
            @RequestParam(value = "page", required = false, defaultValue = "0")
                    Integer page,
            @ApiParam(required = false, defaultValue = "20", value = "한 페이지의 행갯수")
            @RequestParam(value = "size", required = false, defaultValue = "20")
                    Integer size
    ) {
        final Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "ctime");
        final Page<BookSearchHistory> result = bookSearchHistoryService.list(
                springSecurityHelper.getUsernameOrThrow(), false,
                bookSearchProviders.getProviderIds(), pageable);
        return result.getContent();
    }

}
