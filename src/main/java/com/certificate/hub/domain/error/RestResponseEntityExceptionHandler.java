package com.certificate.hub.domain.error;

import com.certificate.hub.domain.dto.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest webRequest) {

        Map<String, String> mapError = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(error ->{
            String key = ((FieldError) error).getField();
            String value = error.getDefaultMessage();
            mapError.put(key, value);
        });

        ErrorMessage message = ErrorMessage.builder()
                .message(mapError.toString())
                .status(HttpStatus.BAD_REQUEST)
                .path(webRequest.getDescription(false).replace("uri=",""))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<ErrorMessage> handlerApiRequestException(ApiRequestException exception, WebRequest webRequest) {
        ErrorMessage message = ErrorMessage.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .path(webRequest.getDescription(false).replace("uri=",""))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }


}
