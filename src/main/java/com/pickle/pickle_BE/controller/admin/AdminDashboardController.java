package com.pickle.pickle_BE.controller.admin;

import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final UserService userService;

    @RequestMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers(); // 모든 사용자를 가져옴
        model.addAttribute("users", users); // 모델에 사용자 목록 추가
        return "admin/manage-users"; // 사용자 관리 페이지로 이동
    }

}
