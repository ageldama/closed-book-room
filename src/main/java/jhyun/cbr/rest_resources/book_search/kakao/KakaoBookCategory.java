package jhyun.cbr.rest_resources.book_search.kakao;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jhyun.cbr.rest_resources.book_search.BookCategory;

@ApiModel("카카오 책검색 카테고리 코드")
public class KakaoBookCategory implements BookCategory {

    @ApiModelProperty(name = "대분류명", required = true, example = "e북")
    private String level1;

    @ApiModelProperty(name = "상세분류명", required = true, example = "리더쉽/경영")
    private String level2;

    @ApiModelProperty(name = "코드값", required = true, example = "111")
    private Integer code;

    public KakaoBookCategory() {
    }

    public KakaoBookCategory(String level1, String level2, Integer code) {
        this.level1 = level1;
        this.level2 = level2;
        this.code = code;
    }

    public static KakaoBookCategory of(String level1, String level2, Integer code) {
        return new KakaoBookCategory(level1, level2, code);
    }

    public String getLevel1() {
        return level1;
    }

    public void setLevel1(String level1) {
        this.level1 = level1;
    }

    public String getLevel2() {
        return level2;
    }

    public void setLevel2(String level2) {
        this.level2 = level2;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KakaoBookCategory that = (KakaoBookCategory) o;
        return Objects.equal(level1, that.level1) &&
                Objects.equal(level2, that.level2) &&
                Objects.equal(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(level1, level2, code);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("level1", level1)
                .add("level2", level2)
                .add("code", code)
                .toString();
    }
}
