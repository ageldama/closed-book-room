package jhyun.cbr.rest_resources.book_search.kakao;

public enum KakaoBookSearchTargets {
    all("all"),
    title("title"),
    isbn("isbn"),
    keyword("keyword"),
    overview("overview"),
    publisher("publisher");

    private String value;

    KakaoBookSearchTargets(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
