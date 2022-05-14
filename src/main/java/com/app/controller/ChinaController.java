package com.app.controller;

import com.app.entity.MyUserDetails;
import com.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/china")
public class ChinaController {
    @Autowired
    private EmailService emailService;

    @RequestMapping(path = "/tiananmen", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getTiananmenPage( Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(((MyUserDetails)authentication.getPrincipal()).getEmail());
        return "china/tiananmen";
    }
    @RequestMapping(path = "/apartheid", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getApartheidPage( Model model) {
        return "china/apartheid";
    }


}