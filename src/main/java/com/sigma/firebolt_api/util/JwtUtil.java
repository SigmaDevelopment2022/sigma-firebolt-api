package com.sigma.firebolt_api.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sigma.firebolt_api.domain.User;
import com.sigma.firebolt_api.domain.UserRole;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;

public class JwtUtil {
    public static Algorithm getAlgorithm() {
        return Algorithm.HMAC256("niagarapopo".getBytes(StandardCharsets.UTF_8));
    }

    public static String createAccessToken(User user, String issuer) {
        long expiration = DateTimeUtil.toMillis(LocalDateTime.now().plusMinutes(10));

        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("ROLE", user.getRole().name())
                .withIssuer(issuer)
                .withExpiresAt(Instant.ofEpochMilli(expiration))
                .sign(getAlgorithm());
    }

    public static String createRefreshToken(User user, String issuer) {
        LocalDateTime tomorrowMidnight = DateTimeUtil.getMidnight(LocalDateTime.now().plusDays(1));
        long expiration = DateTimeUtil.toMillis(tomorrowMidnight);

        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("ROLE", UserRole.NO_ROLE.name())
                .withIssuer(issuer)
                .withExpiresAt(Instant.ofEpochMilli(expiration))
                .sign(getAlgorithm());
    }

    public static User decodeToken(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return User.builder()
                .username(decodedJWT.getSubject())
                .role(UserRole.valueOf(decodedJWT.getClaim("ROLE").asString()))
                .build();
    }
}
