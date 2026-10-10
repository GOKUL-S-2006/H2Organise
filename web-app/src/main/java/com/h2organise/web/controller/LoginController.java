package com.h2organise.web.controller;

import com.h2organise.web.client.ApiClient;
import com.h2organise.web.client.AuthClient;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AuthClient authClient;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        HttpSession session, Model model) {
        Map<String, Object> user;
        try {
            user = authClient.login(email, password);
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Wrong email or password");
            return "login";
        }
        session.setAttribute("user", user);
        return "CITIZEN".equals(user.get("role")) ? "redirect:/my/complaints" : "redirect:/dashboard";
    }

    // citizens make their own account here
    @PostMapping("/register")
    public String register(@RequestParam Map<String, String> form, RedirectAttributes ra) {
        authClient.register(ApiClient.clean(form));
        ra.addFlashAttribute("msg", "Account created, you can log in now");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
