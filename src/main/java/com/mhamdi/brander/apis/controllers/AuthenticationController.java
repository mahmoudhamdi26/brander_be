package com.mhamdi.brander.apis.controllers;

import com.mhamdi.brander.apis.dto.request.LoginRequest;
import com.mhamdi.brander.apis.dto.request.SignUpRequest;
import com.mhamdi.brander.apis.dto.response.AppResponse;
import com.mhamdi.brander.apis.dto.response.JwtAuthenticationResponse;
import com.mhamdi.brander.exceptions.apis.ApplicationException;
import com.mhamdi.brander.exceptions.apis.Errors;
import com.mhamdi.brander.services.intrfaces.AuthenticationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        try{
            JwtAuthenticationResponse res = authenticationService.signup(request);
            return ResponseEntity.ok(res);
        }catch (RuntimeException ex){
            throw new ApplicationException(Errors.USER_NOT_FOUND, Map.of("username", request.getUsername()));
        }
    }

    @PostMapping("/login")
    // @CrossOrigin(origins = "http://localhost:9000")
    public ResponseEntity<AppResponse> signin(@RequestBody LoginRequest request) {
        try{
            AppResponse res = authenticationService.signin(request);
            return ResponseEntity.ok(res);
        }catch (RuntimeException ex){
            throw new ApplicationException(Errors.USER_NOT_FOUND, Map.of("username", request.getUsername()));
        }
    }
}
