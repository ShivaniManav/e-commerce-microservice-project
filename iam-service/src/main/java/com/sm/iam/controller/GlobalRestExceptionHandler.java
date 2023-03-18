package com.sm.iam.controller;

import com.sm.iam.dto.response.IamErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalRestExceptionHandler {
    
    @ExceptionHandler
    public ResponseEntity<IamErrorResponse> handleException(Exception ex) {
        IamErrorResponse error = new IamErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<IamErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }
    
}
