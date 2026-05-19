package com.example.backform.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    private final SecureRandom secureRandom = new SecureRandom();
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Value("${app.auth.token-expire-minutes:120}")
    private long tokenTtlMinutes;

    public String issue(CurrentUser user) {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(tokenBytes);

        Instant expiresAt = Instant.now().plusSeconds(tokenTtlMinutes * 60);
        Session session = new Session(user, expiresAt, false);
        sessions.put(token, session);

        return token;
    }

    public CurrentUser verify(String token) {
        Session session = sessions.get(token);
        if (session == null) {
            return null;
        }

        if (session.revoked()) {
            sessions.remove(token);
            return null;
        }

        if (session.expiresAt().isBefore(Instant.now())) {
            sessions.remove(token);
            return null;
        }

        return session.user();
    }

    public void revoke(String token) {
        Session existingSession = sessions.get(token);
        if (existingSession == null) {
            return;
        }

        Session revokedSession = new Session(
                existingSession.user(),
                existingSession.expiresAt(),
                true
        );
        sessions.put(token, revokedSession);
        sessions.remove(token);
    }

    private record Session(
            CurrentUser user,
            Instant expiresAt,
            boolean revoked
    ) {
    }
}
