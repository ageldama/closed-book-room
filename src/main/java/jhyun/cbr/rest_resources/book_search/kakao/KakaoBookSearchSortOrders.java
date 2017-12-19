package jhyun.cbr.rest_resources.book_search.kakao;

/** 카카오 책검색 정렬기준 */
public enum KakaoBookSearchSortOrders {
    /** 정확도순 */
    accuracy("accuracy"),

    /** 최신순 */
    recency("recency"),

    /** 판매량순 */
    sales("sales");

    private String value;

    KakaoBookSearchSortOrders(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
