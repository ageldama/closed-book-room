package jhyun.cbr.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jhyun.cbr.rest_resources.book_search.kakao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class KakaoBookSearchService implements BookSearchService<KakaoBookSearchRequest,
        KakaoBookSearchResult, KakaoBookCategory> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private String restApiKey;
    private RestTemplate restTemplate;
    private KakaoRequestToQueryStringService requestToQueryStringService;
    private ObjectMapper objectMapper;

    public KakaoBookSearchService(
            @Value("${closed-book-room.kakao-api.rest-api-key}")
                    String restApiKey,
            @Autowired
                    RestTemplate restTemplate,
            @Autowired
                    KakaoRequestToQueryStringService requestToQueryStringService,
            @Autowired
                    ObjectMapper objectMapper
    ) {
        this.restApiKey = restApiKey;
        this.requestToQueryStringService = requestToQueryStringService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private String authorizationHeader() {
        final String AUTHORIZATION_HEADER_FMT = "KakaoAK %s";
        final String REST_API_KEY = "9b7471bbb7d0d1405fa11afbca9f4856";
        return String.format(AUTHORIZATION_HEADER_FMT, REST_API_KEY);
    }

    @Override
    public KakaoBookSearchRequest parseJsonAsRequest(String jsonStr) {
        try {
            return objectMapper.readValue(jsonStr, KakaoBookSearchRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public KakaoBookSearchResult search(KakaoBookSearchRequest request) {
        final String url = String.format("https://dapi.kakao.com/v2/search/book?%s",
                requestToQueryStringService.buildQueryString(request));
        LOGGER.debug("URL = {}", url);
        //
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authorizationHeader());
        final HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        final ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                final String body = response.getBody();
                return objectMapper.readValue(body, KakaoBookSearchResult.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            final String mesg = String.format("%s %s %s",
                    response.getStatusCode().value(), response.getStatusCode().getReasonPhrase(), response.getBody());
            throw new RuntimeException(mesg);
        }
    }

    @Override
    public List<KakaoBookCategory> categories() {
        return KakaoBookCategories.CATEGORIES;
    }

    @Override
    public String getProviderId() {
        return KakaoConsts.PROVIDER_ID;
    }

}
