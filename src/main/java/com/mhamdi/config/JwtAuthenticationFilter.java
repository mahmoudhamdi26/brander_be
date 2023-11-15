package com.mhamdi.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhamdi.brander.exceptions.apis.ApplicationException;
import com.mhamdi.brander.exceptions.apis.Errors;
import com.mhamdi.brander.services.intrfaces.JwtService;
import com.mhamdi.brander.services.intrfaces.UserService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String reqPath = request.getRequestURL().toString();            
        if(!reqPath.contains("/api/")){
            filterChain.doFilter(request, response);
            return;    
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;        
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer")) {                        
            if(reqPath.contains("/auth/login") || reqPath.contains("/auth/register")){
                filterChain.doFilter(request, response);
                return;    
            }
           Map<String, Object> errorDetails = new HashMap<>();
           errorDetails.put("status", 401);
           errorDetails.put("message", "Unauthorized");
           errorDetails.put("errorCode", "USER_NOT_FOUND");
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           response.setContentType(MediaType.APPLICATION_JSON_VALUE);
           ObjectMapper mapper = new ObjectMapper();
           mapper.writeValue(response.getWriter(), errorDetails);
            // filterChain.doFilter(request, response);
            // return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUserName(jwt);
        if (StringUtils.isNotEmpty(userEmail)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService()
                .loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
                filterChain.doFilter(request, response);
            }else{
                writeError(response);
            }            
        }else{
            writeError(response);
        }
    }

    private void writeError(HttpServletResponse response) throws StreamWriteException, DatabindException, IOException{
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", 401);
        errorDetails.put("message", "Unauthorized");
        errorDetails.put("errorCode", "USER_NOT_FOUND");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), errorDetails);
    } 
}