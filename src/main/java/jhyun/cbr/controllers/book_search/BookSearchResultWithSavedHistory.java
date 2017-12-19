package jhyun.cbr.controllers.book_search;

import com.google.common.base.Objects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jhyun.cbr.rest_resources.book_search.BookSearchResult;

@ApiModel("책 검색 결과 + 해당 검색건의 히스토리 ID")
public class BookSearchResultWithSavedHistory {
    @ApiModelProperty(name = "검색 결과", required = true)
    private BookSearchResult bookSearchResult;

    @ApiModelProperty(name = "검색건의 히스토리 ID", required = true)
    private Long historyId;

    public BookSearchResultWithSavedHistory() {
    }

    public BookSearchResultWithSavedHistory(BookSearchResult bookSearchResult, Long historyId) {
        this.bookSearchResult = bookSearchResult;
        this.historyId = historyId;
    }

    public BookSearchResult getBookSearchResult() {
        return bookSearchResult;
    }

    public void setBookSearchResult(BookSearchResult bookSearchResult) {
        this.bookSearchResult = bookSearchResult;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookSearchResultWithSavedHistory that = (BookSearchResultWithSavedHistory) o;
        return Objects.equal(bookSearchResult, that.bookSearchResult) &&
                Objects.equal(historyId, that.historyId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bookSearchResult, historyId);
    }

    @Override
    public String toString() {
        return "BookSearchResultWithSavedHistory{" +
                "bookSearchResult=" + bookSearchResult +
                ", historyId=" + historyId +
                '}';
    }
}
