package com.example.bookreview.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RequestSanitizer implements HandlerInterceptor {

    private static final Map<String, Set<String>> ALLOWED_PARAMS = Map.of(
            "/books", Set.of("id"),
            "/books/search", Set.of("title", "author"),
            "/books/stats", Set.of(),
            "/reviews", Set.of("bookId", "id")
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();

        // Ukloni trailing slash ako ga ima
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        Set<String> allowed = ALLOWED_PARAMS.get(path);
        if (allowed == null) return true; // putanja nije u mapi — ne proveravamo ništa

        Set<String> params = request.getParameterMap().keySet();

        // pronađi sve nedozvoljene parametre
        Set<String> invalid = params.stream()
                .filter(p -> !allowed.contains(p))
                .collect(Collectors.toSet());

        if (!invalid.isEmpty()) {
            String invalidParams = invalid.stream()
                    .map(p -> "'" + p + "'")
                    .collect(Collectors.joining(", "));

            throw new IllegalArgumentException("Unknown query parameter" +
                    (invalid.size() > 1 ? "s: " : ": ") + invalidParams);
        }

        return true;
    }
}
