package jhyun.cbr.controllers.http_status_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN,
        reason = "Given BookSearchHistory is belonged to another user like all your bases are.")
public class BookSearchHistoryOfAnotherUserException extends RuntimeException {
    private Long givenBookSearchHistoryId;

    public BookSearchHistoryOfAnotherUserException(Long givenBookSearchHistoryId) {
        this.givenBookSearchHistoryId = givenBookSearchHistoryId;
    }
}
