package jhyun.cbr.services;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import jhyun.cbr.rest_resources.book_search.kakao.KakaoBookSearchRequest;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class KakaoRequestToQueryStringService {
    public Map<String, Object> buildMap(final KakaoBookSearchRequest request) {
        checkNotNull(request);
        final Map<String, Object> it = new HashMap<>();
        it.put("query", Strings.nullToEmpty(request.getQuery()));
        ImmutableSet.<ImmutablePair<String, Supplier<Object>>>of(
                new ImmutablePair<>("sort", () -> request.getSort() == null ? null : request.getSort().getValue()),
                new ImmutablePair<>("page", () -> request.getPage()),
                new ImmutablePair<>("size", () -> request.getSize()),
                new ImmutablePair<>("target", () -> request.getTarget() == null ? null : request.getTarget().getValue()),
                new ImmutablePair<>("category", () -> request.getCategory())
        ).stream().forEach(pair -> {
            final Object v = pair.getRight().get();
            if (v != null) {
                it.put(pair.getLeft(), v);
            }
        });
        return it;
    }

    public String buildQueryString(final KakaoBookSearchRequest request) {
        final Joiner joiner = Joiner.on("&");
        final List<String> parts = buildMap(request).entrySet().stream()
                .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return joiner.join(parts);
    }
}
