package com.mhamdi.brander.services.intrfaces;

import com.mhamdi.brander.apis.dto.request.LoginRequest;
import com.mhamdi.brander.apis.dto.request.SignUpRequest;
import com.mhamdi.brander.apis.dto.response.AppResponse;
import com.mhamdi.brander.apis.dto.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    AppResponse signin(LoginRequest request);
}
