package com.app.controller;

import com.app.entity.Instructor;
import com.app.entity.Librarian;
import com.app.entity.Student;
import com.app.entity.User;
import com.app.helpers.Role;
import com.app.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class LoginController {
    private final UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping("/login")
    public String index() {
        //System.out.println((new Date(System.currentTimeMillis()).toString()).replace("-", ""));
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

    @Transactional
    @PostMapping("register")
    public String registerUser(@RequestParam String bilkentId, @RequestParam String email, @RequestParam String password,
                               @RequestParam String firstName, @RequestParam String lastName, @RequestParam int userRole,
                               @RequestParam String year, @RequestParam String department,
                               @RequestParam String sections, @RequestParam String yearsOfExperience,
                               Model model) throws Exception {
        List<User> users = userService.findAll();
        for (User user: users) {
            if (email.equals(user.getEmail())) {
                model.addAttribute("errorMessage", "User with this email already exists");
                return "/register";
            }
            if (bilkentId.equals(user.getBilkentId())) {
                model.addAttribute("errorMessage", "User with this Bilkent ID already exists");
                return "/register";
            }
        }

        if (!email.contains("bilkent.edu.tr")) {
            String errorMessage = "Email has to include 'bilkent.edu.tr'";
            model.addAttribute("errorMessage", errorMessage);
            return "/register";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setBilkentId(bilkentId);
        user.setFirst_name(firstName);
        user.setLast_name(lastName);



        if (userRole == 0) {
            user.setRole(Role.STUDENT);
            System.out.println(user.getId());
            userService.save(user);
            entityManager.createNativeQuery("INSERT INTO student (department, fines, year, user_id) VALUES (?,?,?,?)")
                    .setParameter(1, department)
                    .setParameter(2, 0)
                    .setParameter(3, Integer.valueOf(year))
                    .setParameter(4, user.getId())
                    .executeUpdate();
        }
        if (userRole == 1) {
            user.setRole(Role.INSTRUCTOR);
            userService.save(user);
            entityManager.createNativeQuery("INSERT INTO instructor (department, fines, sections, user_id) VALUES (?,?,?,?)")
                    .setParameter(1, department)
                    .setParameter(2, 0)
                    .setParameter(3, sections)
                    .setParameter(4, user.getId())
                    .executeUpdate();
        }
        if (userRole == 2) {
            user.setRole(Role.LIBRARIAN);
            userService.save(user);
            entityManager.createNativeQuery("INSERT INTO librarian (years_of_experience, user_id) VALUES (?,?)")
                    .setParameter(1, Integer.valueOf(yearsOfExperience))
                    .setParameter(2, user.getId())
                    .executeUpdate();

        }



        return "login";
    }


}
