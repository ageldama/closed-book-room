package jhyun.cbr.services.exceptions;

public class BookSearchHistoryBookmarkExistingException extends Exception {
    private Long historyId;

    public BookSearchHistoryBookmarkExistingException(Long historyId) {
        this.historyId = historyId;
    }

    public Long getHistoryId() {
        return historyId;
    }
}
