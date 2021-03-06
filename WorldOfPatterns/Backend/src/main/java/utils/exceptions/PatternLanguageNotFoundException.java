package utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such pattern language")  // 404
public class PatternLanguageNotFoundException extends RuntimeException {
}
