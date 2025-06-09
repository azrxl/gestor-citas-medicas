package cr.ac.una.demologinspringboot.logic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidScheduleFormatException extends RuntimeException {
    public InvalidScheduleFormatException(String message) {
        super(message);
    }

    public InvalidScheduleFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}