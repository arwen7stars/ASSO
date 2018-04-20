package utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such revision exists or the pattern is invalid")  // 404
public class OldRevisionNotFound extends RuntimeException {
}
