package jhyun.cbr.rest_resources.book_search.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.util.List;

/**
 * "authors": [
 * "편집부"
 * ],
 * "barcode": "KOR7051820000001",
 * "category": "어린이전집",
 * "contents": "",
 * "datetime": "2011-07-02T00:00:00.000+09:00",
 * "ebook_barcode": "",
 * "isbn": " 7051820000001",
 * "price": 680000,
 * "publisher": "한국톨스토이",
 * "sale_price": 200000,
 * "sale_yn": "N",
 * "status": "가격 확인 중",
 * "thumbnail": "http://t1.daumcdn.net/thumb/P72x100/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fbook%2FKOR7051820000001%3Fmoddttm=20170112154427",
 * "title": "쫑알쫑알한글똑똑/인성동화/유아한글동화/유아한글동화/한글놀이책/한글창작동화",
 * "translators": [
 * "한국톨스토이"
 * ],
 * "url": "http://book.daum.net/detail/book.do?bookid=KOR7051820000001"
 */
public class KakaoBookSearchEntry {
    private List<String> authors;
    private String barcode;

    private String category;

    private String contents;

    private String datetime;

    @JsonProperty("ebook_barcode")
    private String ebookBarcode;

    private String isbn;

    private BigDecimal price;

    private String publisher;

    @JsonProperty("sale_price")
    private BigDecimal salePrice;

    @JsonProperty("sale_yn")
    private String saleYn;

    private String status;

    private String thumbnail;

    private String title;

    private List<String> translators;

    private String url;

    public KakaoBookSearchEntry() {
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getEbookBarcode() {
        return ebookBarcode;
    }

    public void setEbookBarcode(String ebookBarcode) {
        this.ebookBarcode = ebookBarcode;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getSaleYn() {
        return saleYn;
    }

    public Boolean isSale() {
        return (!Strings.isNullOrEmpty(saleYn) && "Y".equalsIgnoreCase(saleYn));
    }

    public void setSale(final Boolean sale) {
        if (sale == null) this.saleYn = null;
        else this.saleYn = sale ? "Y" : "N";
    }

    public void setSaleYn(String saleYn) {
        this.saleYn = saleYn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTranslators() {
        return translators;
    }

    public void setTranslators(List<String> translators) {
        this.translators = translators;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KakaoBookSearchEntry that = (KakaoBookSearchEntry) o;
        return Objects.equal(authors, that.authors) &&
                Objects.equal(barcode, that.barcode) &&
                Objects.equal(category, that.category) &&
                Objects.equal(contents, that.contents) &&
                Objects.equal(datetime, that.datetime) &&
                Objects.equal(ebookBarcode, that.ebookBarcode) &&
                Objects.equal(isbn, that.isbn) &&
                Objects.equal(price, that.price) &&
                Objects.equal(publisher, that.publisher) &&
                Objects.equal(salePrice, that.salePrice) &&
                Objects.equal(saleYn, that.saleYn) &&
                Objects.equal(status, that.status) &&
                Objects.equal(thumbnail, that.thumbnail) &&
                Objects.equal(title, that.title) &&
                Objects.equal(translators, that.translators) &&
                Objects.equal(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authors, barcode, category, contents, datetime,
                ebookBarcode, isbn, price, publisher, salePrice, saleYn, status,
                thumbnail, title, translators, url);
    }

    @Override
    public String toString() {
        return "KakaoBookSearchEntry{" +
                "authors=" + authors +
                ", barcode='" + barcode + '\'' +
                ", category='" + category + '\'' +
                ", contents='" + contents + '\'' +
                ", datetime='" + datetime + '\'' +
                ", ebookBarcode='" + ebookBarcode + '\'' +
                ", isbn='" + isbn + '\'' +
                ", price=" + price +
                ", publisher='" + publisher + '\'' +
                ", salePrice=" + salePrice +
                ", saleYn='" + saleYn + '\'' +
                ", status='" + status + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", title='" + title + '\'' +
                ", translators=" + translators +
                ", url='" + url + '\'' +
                '}';
    }
}

