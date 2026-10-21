package com.project.bitly.config;

import com.project.bitly.user.AppUser;
import com.project.bitly.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class UserProvisioningAspect {

    private final UserService userService;
    private final AppUserRepository appUserRepository;

    @Around("@annotation(com.project.bitly.config.EnsureLocalUser)")
    public Object ensureUser(ProceedingJoinPoint joinPoint) throws Throwable {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("Authentication required");
        }

        String provider;
        String subject;
        String email;

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            provider = jwt.getIssuer().toString();
            subject = jwt.getSubject();
            email = jwt.getClaimAsString("email");

        } else if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            provider = oidcUser.getIssuer().toString();
            subject = oidcUser.getSubject();
            email = oidcUser.getEmail();

        } else {
            throw new IllegalStateException("Unsupported authentication type: " + authentication.getClass().getName());
        }

        if (!userService.userExists(provider, subject)) {
            AppUser user = new AppUser();
            user.setId(UUID.randomUUID());
            user.setProvider(provider);
            user.setProviderSubject(subject);
            user.setEmail(email);

            appUserRepository.save(user);
        }

        return joinPoint.proceed();
    }


}