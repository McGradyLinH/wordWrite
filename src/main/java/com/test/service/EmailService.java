package com.test.service;

import java.util.List;

public interface EmailService {
    void sendSimpleEmail(String to,String subject,String content);

    boolean sendFileEmail(String to, String subject, String content, List filepath);

}
