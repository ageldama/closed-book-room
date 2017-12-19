package jhyun.cbr.controllers;

import jhyun.cbr.controllers.http_status_exceptions.NotAuthorizedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityHelper {
    public Optional<String> getUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null) {
            return Optional.of(authentication.getName());
        } else {
            return Optional.empty();
        }
    }

    /**
     * @throws NotAuthorizedException
     */
    public String getUsernameOrThrow() {
        final Optional<String> usernameOptional = getUsername();
        if (usernameOptional.isPresent()) return usernameOptional.get();
        else throw new NotAuthorizedException();
    }
}
