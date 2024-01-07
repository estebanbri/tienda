package com.example.tiendabackend.advise;

import com.example.tiendabackend.dto.ApiErrorDTO;
import com.example.tiendabackend.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvise {

    private static final Logger log = LoggerFactory.getLogger(GlobalControllerAdvise.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorDTO> apiException(ApiException e) {
        log.error("Api Exception: {} ", e.getMessage());
        ApiErrorDTO apiError = new ApiErrorDTO();
        apiError.status_code = e.statusCode();
        apiError.message = e.getMessage();
        return ResponseEntity.status(e.statusCode()).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(ex.getParameter() + ", " + error.getDefaultMessage()));
        log.error("Input validation failed errors: {}", errors);
        ApiErrorDTO apiError = new ApiErrorDTO();
        apiError.message = errors.toString();
        apiError.status_code = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> serverError(Exception e) {
        log.error("Internal Server error: {} ", e.getMessage());
        ApiErrorDTO apiError = new ApiErrorDTO();
        apiError.status_code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        apiError.message = e.getMessage();
        return ResponseEntity.internalServerError().body(apiError);
    }

}
