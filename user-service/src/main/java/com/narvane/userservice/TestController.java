package com.narvane.userservice;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    // Approach A: Access the Authentication object
    @GetMapping("/test-auth")
    public Map<String, Object> testAuth(Authentication authentication) {
        // 'authentication' holds the user's principal details
        // in a JWT-based flow, typically an instance of JwtAuthenticationToken

        Map<String, Object> result = new HashMap<>();
        result.put("principal", authentication.getName());
        result.put("authorities", authentication.getAuthorities());
        return result;
    }

    // Approach B: Directly inject the JWT
    @GetMapping("/test-jwt")
    public Map<String, Object> testJwt(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> result = new HashMap<>();
        result.put("subject", jwt.getSubject());  // "sub" claim
        result.put("issuer", jwt.getIssuer());
        result.put("claims", jwt.getClaims());
        return result;
    }
}
