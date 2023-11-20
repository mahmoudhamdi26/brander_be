package com.mhamdi.brander.exceptions.apis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler { //extends DefaultErrorAttributes {

//    private final MessageSource messageSource;

    @ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<ExceptionResponse> mediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        // create your custom JSON response object
        ExceptionResponse resp = new ExceptionResponse(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED_MEDIA_TYPE");
        // return the response with the appropriate status code
        return new ResponseEntity<ExceptionResponse>(resp, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }


    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Map<String, Object>> handleException(ApplicationException ex,
                                                               WebRequest request) {
        log.error("Application exception occurred {}", ex.getMessage());
        ex.printStackTrace();
        return createResponse(request, ex.getErrorResponse().getHttpStatus(), ex.getMessage(), null, null);
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleException(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        log.error("Validation error occurred {}", ex.getMessage());
        List<ConstraintsViolationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ConstraintsViolationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return createResponse(request, HttpStatus.BAD_REQUEST, ex.getMessage(), null, validationErrors);
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(jakarta.servlet.ServletException.class)
    public ResponseEntity<Map<String, Object>> handleException(jakarta.servlet.ServletException ex,
                                                               WebRequest request) {
        log.error("Application exception occurred {}", ex.getMessage());
        return createResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null, null);
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleException(org.springframework.dao.DataIntegrityViolationException ex,
                                                               WebRequest request) {
        log.error("Application exception occurred {}", ex.getMessage());
        return createResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null, null);
    }

    private ResponseEntity<Map<String, Object>> createResponse(WebRequest request, HttpStatus status, String message, Object data, List<ConstraintsViolationError> errors) {
        // create a map to store the response information
        Map<String, Object> map = new HashMap<>();
        // put the status, message, data, errors, timestamp, and path to the map
        map.put("status", status.value());
        map.put("message", message);
        map.put("data", data);
        map.put("errors", errors);
        map.put("timestamp", new Date());
        map.put("path", request.getDescription(false));
        // create and return the ResponseEntity object with the given status and body
        return ResponseEntity.status(status).body(map);
    }

    // You can define methods to handle specific exceptions, such as AuthenticationException or AccessDeniedException
    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<ExceptionResponse> authenticationException(AuthenticationException ex) {
        // You can create your custom JSON response object using the ExceptionResponse class
        ExceptionResponse resp = new ExceptionResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, "AUTH_ERROR");
        // You can return the response with the appropriate status code
        return new ResponseEntity<ExceptionResponse>(resp, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ExceptionResponse> accessDeniedException(AccessDeniedException ex) {
        // You can create your custom JSON response object using the ExceptionResponse class
        ExceptionResponse resp = new ExceptionResponse(ex.getMessage(), HttpStatus.FORBIDDEN, "ACCESS_DENIED");
        // You can return the response with the appropriate status code
        return new ResponseEntity<ExceptionResponse>(resp, HttpStatus.FORBIDDEN);
    }



//    protected ResponseEntity<Map<String, Object>> ofType(WebRequest request,
//                                                         HttpStatus status, ApplicationException ex) {
//        Locale locale = LocaleContextHolder.getLocale();
//        return ofType(request, status, ex.getLocalizedMessage(locale, messageSource), ex.getErrorResponse().getKey(), Collections.EMPTY_LIST);
//    }
//
//    private ResponseEntity<Map<String, Object>> ofType(WebRequest request, HttpStatus status, String message,
//                                                       String key, List validationErrors) {
//        Map<String, Object> attributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
//        attributes.put("status", status.value());
//        attributes.put("error", status);
//        attributes.put("message", message);
//        attributes.put("errors", validationErrors);
//        attributes.put("error_key", key);
//        attributes.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
//        return new ResponseEntity<>(attributes, status);
//    }
}