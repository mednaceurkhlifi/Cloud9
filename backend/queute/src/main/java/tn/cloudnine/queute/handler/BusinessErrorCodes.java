package tn.cloudnine.queute.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {

    BAD_REQUEST_WK(400, HttpStatus.BAD_REQUEST, "Bad request");

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}
