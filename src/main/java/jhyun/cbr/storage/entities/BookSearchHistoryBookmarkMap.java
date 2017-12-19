package jhyun.cbr.storage.entities;

import com.google.common.base.Objects;

import javax.persistence.*;

@Table(name = "book_search_history_to_bookmark_map",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"history_id", "bookmark_id"})
        })
@Entity
public class BookSearchHistoryBookmarkMap {
    @Id
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "bookmark_id")
    private Long bookmarkId;

    public BookSearchHistoryBookmarkMap() {
    }

    public BookSearchHistoryBookmarkMap(Long historyId, Long bookmarkId) {
        this.historyId = historyId;
        this.bookmarkId = bookmarkId;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Long getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(Long bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookSearchHistoryBookmarkMap that = (BookSearchHistoryBookmarkMap) o;
        return Objects.equal(historyId, that.historyId) &&
                Objects.equal(bookmarkId, that.bookmarkId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(historyId, bookmarkId);
    }

    @Override
    public String toString() {
        return "BookSearchHistoryBookmarkMap{" +
                "historyId=" + historyId +
                ", bookmarkId=" + bookmarkId +
                '}';
    }
}
