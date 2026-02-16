package com.example.smartCollege;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${app.api.key}")
    private String apiKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestKey = request.getHeader("X-API-KEY");

        if (apiKey.equals(requestKey)) {
            return true; // Key matches, proceed to Controller
        }

        // Key is missing or wrong
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid or Missing API Key");
        return false; 
    }
}