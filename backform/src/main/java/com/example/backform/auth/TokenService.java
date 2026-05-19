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
    private final SecureRandom random = new SecureRandom();
    private final Map<String,Session> sessions = new ConcurrentHashMap<>();
    @Value("${app.auth.token-expire-minutes:120}") private long ttl;
    public String issue(CurrentUser u){ byte[] b=new byte[32]; random.nextBytes(b); String t=Base64.getUrlEncoder().withoutPadding().encodeToString(b); sessions.put(t,new Session(u,Instant.now().plusSeconds(ttl*60))); return t; }
    public CurrentUser verify(String t){ Session s=sessions.get(t); if(s==null||s.expiresAt.isBefore(Instant.now())){sessions.remove(t); return null;} return s.user; }
    public void revoke(String t){ sessions.remove(t);} private record Session(CurrentUser user, Instant expiresAt){}
}
