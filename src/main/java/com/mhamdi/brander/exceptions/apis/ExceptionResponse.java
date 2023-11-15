package com.mhamdi.brander.exceptions.apis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

// This is the custom ExceptionResponse class
@NoArgsConstructor
public class ExceptionResponse {
    // You can add any fields you want, such as message, status, error code, etc.
    private String message;
    private HttpStatus status;
    private String errorCode;

    // You can also add constructors, getters, setters, and other methods
    public ExceptionResponse(String message, HttpStatus status, String errorCode) {
        this.message = message;
        this.status = status;
        this.errorCode = errorCode;
    }

    // You can use annotations to customize the JSON serialization of this class
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getMessage() {
        return message;
    }

    @JsonProperty("http_status")
    public HttpStatus getStatus() {
        return status;
    }

    @JsonProperty("error_code")
    public String getErrorCode() {
        return errorCode;
    }
}
