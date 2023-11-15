package com.mhamdi.brander.exceptions.apis;

import org.springframework.http.HttpStatus;

public enum Errors implements ErrorResponse {
    USER_NOT_FOUND( "USER_NOT_FOUND", HttpStatus.NOT_FOUND , "User with id {id} not found"), 
    ID_NOT_FOUND( "ID_NOT_FOUND", HttpStatus.NOT_FOUND , "Object with id {id} not found"), 
    CREATE_ERROR( "CREATE_ERROR", HttpStatus.BAD_REQUEST , "Error Processing"),
    DISK_ERROR( "DISK_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "Error While Disk Operation");
    

    String key;
    HttpStatus httpStatus;
    String message;

    Errors(String key, HttpStatus httpStatus, String message) {
        this.message = message;
        this.key = key;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}