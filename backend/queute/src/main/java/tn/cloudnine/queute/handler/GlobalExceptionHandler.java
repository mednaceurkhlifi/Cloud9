package tn.cloudnine.queute.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tn.cloudnine.queute.exception.WorkspaceBadRequest;


import static org.springframework.http.HttpStatus.*;
import static tn.cloudnine.queute.handler.BusinessErrorCodes.BAD_REQUEST_WK;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WorkspaceBadRequest.class)
    public ResponseEntity<ExceptionResponse> handleException(WorkspaceBadRequest exp) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BAD_REQUEST_WK.getCode())
                                .businessErrorDescription(BAD_REQUEST_WK.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }


}
