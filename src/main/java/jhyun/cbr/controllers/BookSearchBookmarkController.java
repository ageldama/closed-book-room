package jhyun.cbr.controllers;

import com.google.common.base.Strings;
import io.swagger.annotations.*;
import jhyun.cbr.controllers.http_status_exceptions.BookSearchHistoryBookmarkAlreadyExistingHttpStatusException;
import jhyun.cbr.controllers.http_status_exceptions.BookSearchHistoryNotFoundException;
import jhyun.cbr.controllers.http_status_exceptions.BookSearchHistoryOfAnotherUserException;
import jhyun.cbr.services.BookSearchHistoryService;
import jhyun.cbr.services.BookSearchProviders;
import jhyun.cbr.services.exceptions.BookSearchHistoryBookmarkExistingException;
import jhyun.cbr.storage.entities.BookSearchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Verify.verify;

@Api(value = "책검색 북마크")
@RequestMapping("/v1/book")
@RestController
public class BookSearchBookmarkController {
    private BookSearchHistoryService bookSearchHistoryService;
    private BookSearchProviders bookSearchProviders;
    private SpringSecurityHelper springSecurityHelper;

    @Autowired
    public BookSearchBookmarkController(
            BookSearchHistoryService bookSearchHistoryService,
            BookSearchProviders bookSearchProviders,
            SpringSecurityHelper springSecurityHelper
    ) {
        this.bookSearchHistoryService = bookSearchHistoryService;
        this.bookSearchProviders = bookSearchProviders;
        this.springSecurityHelper = springSecurityHelper;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "로그인 필요")
    })
    @ApiOperation(value = "북마크 리스팅")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/search-bookmark", method = RequestMethod.GET)
    public Page<BookSearchHistory> list(
            @ApiParam(value = "페이지 번호 (0부터 시작)", required = false, defaultValue = "0")
            @RequestParam(value = "page", required = false, defaultValue = "0")
                    Integer page,
            @ApiParam(value = "페이지 크기 (행 갯수)", required = false, defaultValue = "20")
            @RequestParam(value = "size", required = false, defaultValue = "20")
                    Integer size,
            @ApiParam(value = "정렬 기준", required = false, defaultValue = "ctime",
                    examples = @Example(value = {
                            @ExampleProperty(value = "providerId"),
                            @ExampleProperty(value = "ctime"),
                    }))
            @RequestParam(value = "sort", required = false, defaultValue = "ctime")
                    String sort,
            @ApiParam(value = "정렬차순", required = false, defaultValue = "DESC",
                    examples = @Example(value = {
                            @ExampleProperty(value = "ASC"),
                            @ExampleProperty(value = "DESC"),
                    }))
            @RequestParam(value = "direction", required = false, defaultValue = "DESC")
                    Sort.Direction direction
    ) {
        final PageRequest pageable = new PageRequest(page, size, direction, sort);
        final Page<BookSearchHistory> result = bookSearchHistoryService.list(
                springSecurityHelper.getUsernameOrThrow(),
                true, bookSearchProviders.getProviderIds(), pageable);
        return result;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 401, message = "로그인 필요"),
            @ApiResponse(code = 403, message = "해당 히스토리 데이터가 다른 사용자의것이라 생성불가"),
            @ApiResponse(code = 404, message = "해당 히스토리 데이터 없음"),
            @ApiResponse(code = 409, message = "해당 히스토리 데이터에 대한 북마크가 이미 존재함"),
    })
    @ApiOperation(value = "검색 히스토리로부터 북마크 생성하기")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/search-bookmark/{searchHistoryId}",
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BookSearchHistory copyHistoryAsBookmark(
            @ApiParam(value = "검색 히스토리 ID", required = true)
            @PathVariable("searchHistoryId")
                    Long searchHistoryId
    ) {
        final String username = springSecurityHelper.getUsernameOrThrow();
        verify(!Strings.isNullOrEmpty(username));
        //
        final Optional<BookSearchHistory> searchHistory =
                bookSearchHistoryService.getById(searchHistoryId);
        if (!searchHistory.isPresent()) {
            // 해당 history-id 존재 안함
            throw new BookSearchHistoryNotFoundException(searchHistoryId);
        }
        //
        if (!username.equals(searchHistory.get().getUsername())) {
            // 해당 history이 현재 사용자것이 아님
            throw new BookSearchHistoryOfAnotherUserException(searchHistoryId);
        }
        //
        try {
            final Optional<BookSearchHistory> bookmark = bookSearchHistoryService.copyAsBookmark(searchHistoryId);
            if (bookmark.isPresent()) {
                return bookmark.get();
            } else {
                // ??? 저장실패
                throw new BookSearchHistoryNotFoundException(searchHistoryId);
            }
        } catch (BookSearchHistoryBookmarkExistingException e) {
            // 해당 히스토리에 대한 북마크 이미 존재함.
            throw new BookSearchHistoryBookmarkAlreadyExistingHttpStatusException(searchHistoryId);
        }
    }

    @ApiOperation(value = "북마크 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "DELETED"),
            @ApiResponse(code = 401, message = "로그인 필요"),
            @ApiResponse(code = 403, message = "해당 북마크 데이터가 다른 사용자의것이라 삭제불가"),
            @ApiResponse(code = 404, message = "해당 북마크 데이터 없음"),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/search-bookmark/{searchBookmarkId}", method = RequestMethod.DELETE)
    public void deleteBookmark(
            @ApiParam(value = "삭제할 북마크 ID", required = true)
            @PathVariable("searchBookmarkId")
                    Long searchBookmarkId
    ) {
        final String username = springSecurityHelper.getUsernameOrThrow();
        verify(!Strings.isNullOrEmpty(username));
        //
        final Optional<BookSearchHistory> searchBookmark =
                bookSearchHistoryService.getById(searchBookmarkId);
        if (!searchBookmark.isPresent()) {
            throw new BookSearchHistoryNotFoundException(searchBookmarkId);
        }
        if (!username.equals(searchBookmark.get().getUsername())) {
            throw new BookSearchHistoryOfAnotherUserException(searchBookmarkId);
        }
        //
        bookSearchHistoryService.deleteById(searchBookmarkId);
    }
}
