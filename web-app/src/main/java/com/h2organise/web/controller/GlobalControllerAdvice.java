package com.h2organise.web.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class GlobalControllerAdvice {

    // so every page (navbar) knows who is logged in
    @ModelAttribute("user")
    public Object user(HttpSession session) {
        return session.getAttribute("user");
    }

    // the service answered with 400 / 404 etc, show its message
    @ExceptionHandler(HttpStatusCodeException.class)
    public String serviceError(HttpStatusCodeException ex, Model model) {
        model.addAttribute("error", ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
        return "error";
    }

    // the service is not running
    @ExceptionHandler(ResourceAccessException.class)
    public String serviceDown(ResourceAccessException ex, Model model) {
        model.addAttribute("error", "Could not reach a backend service. Is it running? (" + ex.getMessage() + ")");
        return "error";
    }
}
