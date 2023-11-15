package com.mhamdi.brander.exceptions.apis;

import org.springframework.http.HttpStatus;

public interface ErrorResponse {

    String getKey();

    String getMessage();

    HttpStatus getHttpStatus();
}