package jhyun.cbr.services;

import jhyun.cbr.rest_resources.book_search.BookCategory;
import jhyun.cbr.rest_resources.book_search.BookSearchRequest;
import jhyun.cbr.rest_resources.book_search.BookSearchResult;

import java.util.List;

public interface BookSearchService<REQ extends BookSearchRequest,
        RES extends BookSearchResult, CAT extends BookCategory> {

    REQ parseJsonAsRequest(String jsonStr);

    RES search(REQ request);

    /** 카테고리 코드 테이블 */
    List<CAT> categories();

    /** 검색 제공자 ID. */
    String getProviderId();
}
