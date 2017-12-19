package jhyun.cbr.rest_resources.book_search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "책 검색결과, 실제 내용은 검색제공자에 따라 다르므로 구현 클래스를 참고.")
@JsonIgnoreProperties(ignoreUnknown = true)
public interface BookSearchResult {
    @ApiModelProperty(required = true, name = "검색기능 제공자ID", example = "kakao")
    String getProviderId();
}
