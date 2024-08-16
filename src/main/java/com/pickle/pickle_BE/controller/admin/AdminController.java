package com.pickle.pickle_BE.controller.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginForm() { return "admin/login";}

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            session.setAttribute("user", user);
            return "redirect:/admin/dashboard"; // 로그인 성공 시 Admin 대시보드 페이지로 리다이렉트
        } else {
            return "redirect:/admin/login?error"; // 로그인 실패 시 다시 로그인 페이지로 리다이렉트
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() { return "admin/dashboard";}
}
