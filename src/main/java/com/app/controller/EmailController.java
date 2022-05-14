package com.app.controller;

import com.app.entity.Application;
import com.app.service.ApplicationService;
import com.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getEmailPage( Model model) {
        return "email";
    }


    @RequestMapping( method = RequestMethod.POST)
    public void sendMail(String email, String subject, String message) {
        emailService.sendMail(email, subject, message);
    }

}