package com.sigma.firebolt_api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.firebolt_api.domain.User;
import com.sigma.firebolt_api.exception.ExceptionDetails;
import com.sigma.firebolt_api.service.UserService;
import com.sigma.firebolt_api.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthorizationFilter extends OncePerRequestFilter{
    private final UserService userService;

    public CustomAuthorizationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION);
        String path = request.getServletPath();
        String bearer = "Bearer ";

        if (!path.equals("/login") && !path.equals("/tokens/refresh") && header != null && header.startsWith(bearer)) {
            String rawToken = header.substring(bearer.length());
            authenticate(rawToken, response);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(String rawToken, HttpServletResponse response) throws IOException {
        User decodeUser = JwtUtil.decodeToken(rawToken);

        if (userService.isBlocked(decodeUser.getUsername())) {
            ExceptionDetails details = ExceptionDetails.builder()
                    .status(HttpStatus.FORBIDDEN.value())
                    .title("Requisição negada")
                    .message("Usuário bloqueado: " + decodeUser.getUsername())
                    .time(LocalDateTime.now().toString())
                    .build();

            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            new ObjectMapper().writeValue(response.getWriter(), details);
            return;
        }

        Collection<SimpleGrantedAuthority> userRole = Collections.singleton(new SimpleGrantedAuthority(decodeUser.getRole().name()));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(decodeUser.getUsername(), null, userRole);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
