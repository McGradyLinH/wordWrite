package com.test.util;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailSendUtil {
    private EmailSendUtil() {
    }

    public static boolean sendMessage(String subject, String to, String content) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.qq.com");
        javaMailSender.setUsername("734652785@qq.com");
        javaMailSender.setPassword("[hmwan170103]");
        javaMailSender.setPort(465);
        javaMailSender.setDefaultEncoding("utf-8");

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setFrom("734652785@qq.com");
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        boolean b = sendMessage("subject", "250520969@qq.com", "test");
        System.out.println(b);
    }
}
