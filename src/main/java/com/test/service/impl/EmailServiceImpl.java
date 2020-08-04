package com.test.service.impl;

import com.test.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmailServiceImpl implements EmailService {
    //从application.properties配置文件中注入发送者的邮件地址
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 注入spring发送邮件的对象
     */
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    @Async
    public boolean sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        try {
            //执行发送
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean sendFileEmail(String to, String subject, String content, List filepath) {
        return false;
    }
}
