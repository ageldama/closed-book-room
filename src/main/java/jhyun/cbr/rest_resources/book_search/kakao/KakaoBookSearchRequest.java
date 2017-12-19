package jhyun.cbr.rest_resources.book_search.kakao;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import jhyun.cbr.rest_resources.book_search.BookSearchRequest;

public class KakaoBookSearchRequest implements BookSearchRequest {
    private String query;
    private KakaoBookSearchSortOrders sort = KakaoBookSearchSortOrders.accuracy;
    private Integer page = 1;
    private Integer size = 10;
    private KakaoBookSearchTargets target = KakaoBookSearchTargets.all;
    private Integer category;

    public KakaoBookSearchRequest() {
    }

    public KakaoBookSearchRequest(String query, KakaoBookSearchSortOrders sort, Integer page, Integer size,
                                  KakaoBookSearchTargets target, Integer category) {
        this.query = query;
        this.sort = sort;
        this.page = page;
        this.size = size;
        this.target = target;
        this.category = category;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public KakaoBookSearchSortOrders getSort() {
        return sort;
    }

    public void setSort(KakaoBookSearchSortOrders sort) {
        this.sort = sort;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public KakaoBookSearchTargets getTarget() {
        return target;
    }

    public void setTarget(KakaoBookSearchTargets target) {
        this.target = target;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KakaoBookSearchRequest that = (KakaoBookSearchRequest) o;
        return Objects.equal(query, that.query) &&
                sort == that.sort &&
                Objects.equal(page, that.page) &&
                Objects.equal(size, that.size) &&
                target == that.target &&
                Objects.equal(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(query, sort, page, size, target, category);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("query", query)
                .add("sort", sort)
                .add("page", page)
                .add("size", size)
                .add("target", target)
                .add("category", category)
                .toString();
    }
}
