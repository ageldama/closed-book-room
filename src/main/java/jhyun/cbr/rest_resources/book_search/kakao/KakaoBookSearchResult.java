package jhyun.cbr.rest_resources.book_search.kakao;

import com.google.common.base.Objects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("카카오 책검색 결과")
public class KakaoBookSearchResult implements jhyun.cbr.rest_resources.book_search.BookSearchResult {
    @ApiModelProperty(required = true, name = "검색 결과 리스트")
    private List<KakaoBookSearchEntry> documents;

    @ApiModelProperty(required = true, name = "검색결과 메타정보")
    private KakaoBookSearchMeta meta;

    public KakaoBookSearchResult() {
    }

    public List<KakaoBookSearchEntry> getDocuments() {
        return documents;
    }

    public void setDocuments(List<KakaoBookSearchEntry> documents) {
        this.documents = documents;
    }

    public KakaoBookSearchMeta getMeta() {
        return meta;
    }

    public void setMeta(KakaoBookSearchMeta meta) {
        this.meta = meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KakaoBookSearchResult that = (KakaoBookSearchResult) o;
        return Objects.equal(documents, that.documents) &&
                Objects.equal(meta, that.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(documents, meta);
    }

    @Override
    public String toString() {
        return "KakaoBookSearchResult{" +
                "documents=" + documents +
                ", meta=" + meta +
                '}';
    }

    @Override
    public String getProviderId() {
        return KakaoConsts.PROVIDER_ID;
    }
}
