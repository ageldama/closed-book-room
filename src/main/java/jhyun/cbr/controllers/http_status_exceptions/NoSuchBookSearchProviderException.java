package jhyun.cbr.controllers.http_status_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "No such book search provider")
public class NoSuchBookSearchProviderException extends RuntimeException {
    private String givenProviderId;

    public NoSuchBookSearchProviderException(String givenProviderId) {
        super();
        this.givenProviderId = givenProviderId;
    }
}
