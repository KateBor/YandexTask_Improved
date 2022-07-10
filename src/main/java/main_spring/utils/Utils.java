package main_spring.utils;

import main_spring.web.response.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utils {
    public static ZonedDateTime StringToZoneDateTime(String StringDate) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        ZonedDateTime date;
        String replacement = "+0000";
        try {
            String d = StringDate.replaceAll("Z$", replacement);
            date = ZonedDateTime.parse(d, dtf);
        } catch (DateTimeParseException e) {
            return null;
        }
        return date;
    }

    public static ResponseEntity<Error> getValidationError() {
        return getError(HttpStatus.BAD_REQUEST, "Validation failed");
    }

    public static ResponseEntity<Error> getNotFoundError() {
        return getError(HttpStatus.NOT_FOUND, "Item not found");
    }

    public static ResponseEntity<Error> getError(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                Error.builder()
                        .code(status)
                        .message(message)
                        .build());
    }
}
