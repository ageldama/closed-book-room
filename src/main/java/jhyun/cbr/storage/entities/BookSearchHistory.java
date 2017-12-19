package jhyun.cbr.storage.entities;

import com.google.common.base.Objects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

/**
 * 도서 검색 히스토리 및 북마크 엔티티.
 * <p>
 * 도서 검색 히스토리와 북마크를 동일한 엔티티로 표현.
 * 북마크는 히스토리가 존재할때만 그에 기반해 복사하여 생성되며,
 * `bookmark`=true인점만 다르고, 한 히스토리 엔티티는 하나의 북마크에만 매핑 가능하다.
 * (한 사용자가 한 검색히스토리건에 대해서 하나의 북마크만 가져야 하니까)
 */
@ApiModel(value = "도서 검색 히스토리 및 북마크")
@Table(name = "book_search_history")
@Entity
public class BookSearchHistory {

    @ApiModelProperty(name = "ID", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "생성일시", required = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ctime")
    private Date ctime;

    @ApiModelProperty(value = "생성한 사용자", required = true)
    @Column(name = "username")
    private String username;

    @ApiModelProperty(value = "책검색 API 제공자 ID", required = true)
    @Column(name = "provider_id")
    private String providerId;

    /**
     * `BookSearchRequest`의 구현 클래스를 여기에 저장
     */
    @ApiModelProperty(value = "책검색 쿼리 요청 내용", required = true)
    @Column(name = "request_json")
    private String requestJson;

    /**
     * 이 문서가 북마크인지 아니면 그냥 검색히스토리인지
     */
    @ApiModelProperty(name = "북마크 여부?", required = true)
    @Column(name = "bookmark")
    private Boolean bookmark;

    public BookSearchHistory() {
    }

    public BookSearchHistory(String username, String providerId, String requestJson, Boolean bookmark) {
        this.username = username;
        this.providerId = providerId;
        this.requestJson = requestJson;
        this.bookmark = bookmark;
        this.ctime = new Date();
    }

    public BookSearchHistory(Long id, Date ctime, String username,
                             String providerId, String requestJson, Boolean bookmark) {
        this.id = id;
        this.ctime = ctime;
        this.username = username;
        this.providerId = providerId;
        this.requestJson = requestJson;
        this.bookmark = bookmark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public Boolean getBookmark() {
        return bookmark;
    }

    public void setBookmark(Boolean bookmark) {
        this.bookmark = bookmark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookSearchHistory that = (BookSearchHistory) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(ctime, that.ctime) &&
                Objects.equal(username, that.username) &&
                Objects.equal(providerId, that.providerId) &&
                Objects.equal(requestJson, that.requestJson) &&
                Objects.equal(bookmark, that.bookmark);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, ctime, username, providerId, requestJson, bookmark);
    }

    @Override
    public String toString() {
        return "BookSearchHistory{" +
                "id=" + id +
                ", ctime=" + ctime +
                ", username='" + username + '\'' +
                ", providerId='" + providerId + '\'' +
                ", requestJson='" + requestJson + '\'' +
                ", bookmark='" + bookmark + '\'' +
                '}';
    }
}
