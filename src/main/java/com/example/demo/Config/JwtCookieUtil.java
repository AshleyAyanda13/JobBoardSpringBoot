package com.example.demo.Config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtCookieUtil {
    @Autowired
    private JwtUtil jwtUtil;

    public String extractUsernameFromCookie(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token == null || !token.contains(".")) {
            throw new RuntimeException("Invalid or missing JWT");
        }
        return jwtUtil.extractUsername(token);
    }

}
