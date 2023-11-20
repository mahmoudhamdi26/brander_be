package com.mhamdi.brander.services.impl;

import com.mhamdi.brander.apis.dto.request.LoginRequest;
import com.mhamdi.brander.apis.dto.request.SignUpRequest;
import com.mhamdi.brander.apis.dto.response.AppResponse;
import com.mhamdi.brander.apis.dto.response.JwtAuthenticationResponse;
import com.mhamdi.brander.exceptions.apis.ApplicationException;
import com.mhamdi.brander.exceptions.apis.Errors;
import com.mhamdi.brander.services.intrfaces.AuthenticationService;
import com.mhamdi.brander.services.intrfaces.JwtService;
import com.mhamdi.db.models.User;
import com.mhamdi.db.models.enums.AccountType;
import com.mhamdi.db.repos.UsersRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) throws RuntimeException {
        var user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(AccountType.USER).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var res = JwtAuthenticationResponse.builder().token(jwt).user(user).build();
        return res;
    }

    @Override
    public AppResponse signin(LoginRequest request) throws RuntimeException{
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApplicationException(Errors.USER_NOT_FOUND, Map.of("id", request.getUsername())));
        var jwt = jwtService.generateToken(user);
        var data = JwtAuthenticationResponse.builder().token(jwt).user(user).build();
        var res = AppResponse.builder().data(data).status(1).message("success").build();
        return res;
    }
}