package com.app.controller;

import com.app.entity.User;
import com.app.service.AttendanceService;
import com.app.service.EventService;
import com.app.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class LoginController {
    private final UserService userService;
    private final EventService eventService;
    private final AttendanceService attendanceService;

    @RequestMapping("/login")
    public String index() {
        System.out.println((new Date(System.currentTimeMillis()).toString()).replace("-", ""));
        return "/login";
    }

    @GetMapping("/login-error")
    public String loginErrorPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            errorMessage = "無效的電子郵件或密碼 ! (Incorrect email or password!)";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "/register";
    }

    @PostMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, @RequestParam String department,
                               @RequestParam String dateOfBirth, @RequestParam int startOfStudies, @RequestParam String instagramUsername,
                               @RequestParam String linkedinUsername, Model model) throws Exception {
        List<User> users = userService.findAll();
        for (User user: users)
            if (email.equals(user.getEmail())) {
                model.addAttribute("errorMessage", "User with this email already exists");
                return "/register";
            }

        if (!email.contains("bilkent.edu.tr")) {
            String errorMessage = "Email has to include 'bilkent.edu.tr'";
            model.addAttribute("errorMessage", errorMessage);
            return "/register";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setDescription("");
        user.setUsername(username);
        user.setDepartment(department);
        if (Long.parseLong(dateOfBirth.replace("-", "")) < (Long.parseLong((new java.sql.Date(System.currentTimeMillis()).toString()).replace("-", "")) - 16)) {
            user.setDateOfBirth(Long.parseLong(dateOfBirth.replace("-", "")));
        }
        else {
            model.addAttribute("errorMessage", "Date of Birth should be at least 16 years less than today's date");
            return "/register";
        }

        if (startOfStudies * 10000 <= Long.parseLong((new java.sql.Date(System.currentTimeMillis()).toString()).replace("-", "")) ) {
            user.setStartOfStudies(startOfStudies);
        }
        else {
            model.addAttribute("errorMessage", "Start of Studies should be less than today's date");
            return "/register";
        }

        if (instagramUsername != "")
            user.setInstagramUsername(instagramUsername);
        if (linkedinUsername != "")
            user.setLinkedinUsername(linkedinUsername);
        userService.save(user);
        return "login";
    }


}
