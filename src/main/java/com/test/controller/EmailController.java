package com.test.controller;

import com.test.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/testsend")
    public boolean sendMail(){
        return emailService.sendSimpleEmail("734652785@qq.com","subject","content");
    }

}
