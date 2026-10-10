package com.h2organise.web.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Map;

// checks that someone is logged in and that their role can open the page
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<?, ?> user = (Map<?, ?>) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        String role = (String) user.get("role");
        String path = request.getRequestURI();

        if (!allowedRoles(path).contains(role)) {
            response.sendRedirect(role.equals("CITIZEN") ? "/my/complaints" : "/dashboard");
            return false;
        }
        return true;
    }

    // same rules as the pages table in the blueprint
    private List<String> allowedRoles(String path) {
        if (path.startsWith("/admin")) {
            return List.of("ADMIN");
        }
        if (path.startsWith("/my")) {
            return List.of("CITIZEN");
        }
        if (path.startsWith("/resources") || path.startsWith("/complaints")) {
            return List.of("ADMIN", "ENGINEER");
        }
        return List.of("ADMIN", "ENGINEER", "OPERATOR");
    }
}
