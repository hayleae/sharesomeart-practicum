package com.example.demo3.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final List<String> PUBLIC_PATHS = Arrays.asList("/login", "/register", "/images");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Get the requested url
        String url = req.getRequestURI();

        // Allow user access to public paths.
        boolean isPublicPath = PUBLIC_PATHS.stream().anyMatch(path -> url.startsWith(path));

        if (isPublicPath) {
            chain.doFilter(request, response);
            return;
        }

        // Get user from session and check if logged in.
        HttpSession session = req.getSession(false);

        boolean isUserLoggedIn = (session != null && session.getAttribute("user") != null);

        // Not logged in? Send back to login page
        if (!isUserLoggedIn) {
            res.sendRedirect("/login.html");
            return;
        }

        chain.doFilter(request, response);
    }

}
