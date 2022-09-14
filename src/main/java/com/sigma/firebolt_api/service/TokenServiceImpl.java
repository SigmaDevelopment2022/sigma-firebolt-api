package com.sigma.firebolt_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.firebolt_api.domain.User;
import com.sigma.firebolt_api.exception.ResourceNotFoundException;
import com.sigma.firebolt_api.mapper.UserMapper;
import com.sigma.firebolt_api.repository.UserRepository;
import com.sigma.firebolt_api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private final UserRepository userRepository;

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String header = request.getHeader(AUTHORIZATION);
        String bearer = "Bearer ";

        if (!header.startsWith(bearer)) {
            return;
        }

        String rawToken = header.substring(bearer.length());
        User decodedUser = JwtUtil.decodeToken(rawToken);
        User user = userRepository.findUsingId(decodedUser.getUsername(), new UserMapper.Full());

        if (user == null) {
            throw new ResourceNotFoundException("Usuário não encontrado: " + decodedUser.getUsername());
        }

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", JwtUtil.createAccessToken(user, request.getRequestURI()));
        tokens.put("refresh_token", rawToken);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), tokens);
    }
}
