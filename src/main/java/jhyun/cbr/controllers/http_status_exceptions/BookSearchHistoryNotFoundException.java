package jhyun.cbr.controllers.http_status_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND,
        reason = "BookSearchHistory cannot be found with given ID")
public class BookSearchHistoryNotFoundException extends RuntimeException {
    private Long givenBookSearchHistoryId;

    public BookSearchHistoryNotFoundException(Long givenBookSearchHistoryId) {
        this.givenBookSearchHistoryId = givenBookSearchHistoryId;
    }
}
