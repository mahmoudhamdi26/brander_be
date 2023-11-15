package com.mhamdi.brander.exceptions.apis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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


//    @ExceptionHandler(ApplicationException.class)
//    public ResponseEntity<Map<String, Object>> handle(ApplicationException ex,
//                                                      WebRequest request) {
//        log.info("Required request body is missing {}", ex.getMessage());
//        return ofType(request, ex.getErrorResponse().getHttpStatus(), ex);
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public final ResponseEntity<Map<String, Object>> handle(
//            MethodArgumentNotValidException ex,
//            WebRequest request) {
//        List<ConstraintsViolationError> validationErrors = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(error -> new ConstraintsViolationError(error.getField(), error.getDefaultMessage()))
//                .collect(Collectors.toList());
//
//        return ofType(request, HttpStatus.BAD_REQUEST, ex.getMessage(), null, validationErrors);
//    }

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