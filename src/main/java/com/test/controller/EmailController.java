package com.test.controller;

import com.test.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;

@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/testsend")
    public boolean sendMail(){
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            // nothing to do
        }
        String pathStr = path.getAbsolutePath();
        // 如果是在eclipse中运行，则和target同级目录,如果是jar部署到服务器，则默认和jar包同级
        pathStr = pathStr.replace("\\target\\classes", "");
        System.err.println(pathStr);
        System.err.println(pathStr);
//        return emailService.sendSimpleEmail("734652785@qq.com","subject","content");
        return false;
    }

}
