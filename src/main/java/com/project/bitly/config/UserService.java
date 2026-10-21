package com.project.bitly.config;


import com.project.bitly.user.AppUser;
import com.project.bitly.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository appUserRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String USER_EXISTS_PREFIX = "user:exists:";

    @Transactional(readOnly = true)
    public boolean userExists(String provider, String subject) {

        String key = USER_EXISTS_PREFIX + provider + ":" + subject;

        // 1. Check Redis first
        String cached = redisTemplate.opsForValue().get(key);

        if (cached != null) {
            return Boolean.parseBoolean(cached);
        }

        // 2. Cache miss: check database
        boolean exists = appUserRepository.existsByProviderAndProviderSubject(provider, subject);

        // 3. Cache result for 10 minutes
        redisTemplate.opsForValue().set(key, Boolean.toString(exists), 10, TimeUnit.MINUTES);

        return exists;
    }

    public AppUser getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken oauth) {
            OAuth2User user = oauth.getPrincipal();
            String email = user.getAttribute("email");
            Optional<AppUser> byEmail = appUserRepository.findByEmail(email);
            if (byEmail.isPresent()) {
                return byEmail.get();
            }
        }
        return null;
    }
}