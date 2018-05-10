package utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Pattern language creation failed")  // 400
public class PatternLanguageCreationFailedException extends RuntimeException {
}
