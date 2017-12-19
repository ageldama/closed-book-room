package jhyun.cbr.controllers;

import com.github.javafaker.Faker;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.when;

public final class SpringSecurityHelperMocks {
    private static final Faker faker = new Faker();

    public static void recordSpringSecurityHelper_noSuchUser(SpringSecurityHelper springSecurityHelper) {
        when(springSecurityHelper.getUsername())
                .thenReturn(Optional.empty());
    }

    public static void recordSpringSecurityHelper_okWithStaticUsername(
            SpringSecurityHelper springSecurityHelper, String username
    ) {
        when(springSecurityHelper.getUsername())
                .thenReturn(Optional.of(username));
    }

    public static CompletableFuture<String> recordSpringSecurityHelper_ok(SpringSecurityHelper springSecurityHelper) {
        final CompletableFuture<String> usernameFuture = new CompletableFuture<>();
        when(springSecurityHelper.getUsername())
                .then(invocationOnMock -> {
                    final String it = faker.gameOfThrones().character();
                    usernameFuture.complete(it);
                    return Optional.of(it);
                });
        return usernameFuture;
    }
}
