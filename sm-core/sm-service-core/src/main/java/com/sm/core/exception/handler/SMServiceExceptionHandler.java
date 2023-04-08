package com.sm.core.exception.handler;

import com.sm.core.dto.response.SMServiceExceptionResponse;
import com.sm.core.exception.SMServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SMServiceExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleException(SMServiceException ex) {

         SMServiceExceptionResponse response = SMServiceExceptionResponse.builder()
                .message(ex.getMessage()).responseCode(ex.getResponseCode())
                .errorCode(ex.getErrorCode()).build();

        return ResponseEntity.ok(response);
    }

}
