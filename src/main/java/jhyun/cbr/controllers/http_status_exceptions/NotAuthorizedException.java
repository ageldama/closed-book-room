package jhyun.cbr.controllers.http_status_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized. Logged username cannot be found on session.")
public class NotAuthorizedException extends RuntimeException {
}
