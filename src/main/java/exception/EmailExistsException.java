package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailExistsException extends RuntimeException {

    public EmailExistsException() {
        super();
    }

    public EmailExistsException(String message) {
        super(message);
    }

    public EmailExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailExistsException(Throwable cause) {
        super(cause);
    }
}
