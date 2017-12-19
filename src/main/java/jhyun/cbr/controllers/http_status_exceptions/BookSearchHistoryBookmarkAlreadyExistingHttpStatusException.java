package jhyun.cbr.controllers.http_status_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,
        reason = "A bookmark is already existing for given search history")
public class BookSearchHistoryBookmarkAlreadyExistingHttpStatusException extends RuntimeException {
    private Long searchHistoryId;

    public BookSearchHistoryBookmarkAlreadyExistingHttpStatusException(Long searchHistoryId) {
        this.searchHistoryId = searchHistoryId;
    }
}
