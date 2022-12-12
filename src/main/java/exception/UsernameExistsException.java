package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UsernameExistsException extends RuntimeException{

    public UsernameExistsException() {
        super();
    }

    public UsernameExistsException(String message) {
        super(message);
    }

    public UsernameExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameExistsException(Throwable cause) {
        super(cause);
    }
}
