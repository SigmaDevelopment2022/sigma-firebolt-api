package com.sigma.firebolt_api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.firebolt_api.domain.User;
import com.sigma.firebolt_api.exception.ExceptionDetails;
import com.sigma.firebolt_api.util.HttpUtil;
import com.sigma.firebolt_api.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = HttpUtil.getUsername(request);
        String password = HttpUtil.getPassword(request);
        UsernamePasswordAuthenticationToken usernamePasswordToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(usernamePasswordToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", JwtUtil.createAccessToken(user, request.getRequestURI()));
        tokens.put("refresh_token", JwtUtil.createRefreshToken(user, request.getRequestURI()));

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        ExceptionDetails details = ExceptionDetails.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .title("Requisição negada")
                .message(failed.getMessage())
                .time(LocalDateTime.now().toString())
                .build();

        if (failed instanceof LockedException) {
            details.setMessage("Usuário bloqueado");
        }

        if (failed instanceof BadCredentialsException) {
            details.setMessage("Usuário ou senha incorretos");
        }

        new ObjectMapper().writeValue(response.getWriter(), details);
    }
}
