package jhyun.cbr.rest_resources.book_search.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("카카오 책검색 API 검색결과 메타정보")
public class KakaoBookSearchMeta {
    @ApiModelProperty(required = true, name = "마지막 페이지인지 여부?")
    @JsonProperty("is_end")
    private Boolean isEnd;

    @ApiModelProperty(required = true, name = "페이지 갯수")
    @JsonProperty("pageable_count")
    private Integer pageableCount;

    @ApiModelProperty(required = true, name="항목건수")
    @JsonProperty("total_count")
    private Integer totalCount;

    public KakaoBookSearchMeta() {
    }

    public Boolean getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Boolean isEnd) {
        this.isEnd = isEnd;
    }

    public Integer getPageableCount() {
        return pageableCount;
    }

    public void setPageableCount(Integer pageableCount) {
        this.pageableCount = pageableCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KakaoBookSearchMeta that = (KakaoBookSearchMeta) o;
        return Objects.equal(isEnd, that.isEnd) &&
                Objects.equal(pageableCount, that.pageableCount) &&
                Objects.equal(totalCount, that.totalCount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isEnd, pageableCount, totalCount);
    }

    @Override
    public String toString() {
        return "KakaoBookSearchMeta{" +
                "isEnd=" + isEnd +
                ", pageableCount=" + pageableCount +
                ", totalCount=" + totalCount +
                '}';
    }
}
