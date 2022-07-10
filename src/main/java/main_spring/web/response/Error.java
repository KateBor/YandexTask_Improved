package main_spring.web.response;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Builder
@Value
public class Error {
    HttpStatus code;
    String message;
}
