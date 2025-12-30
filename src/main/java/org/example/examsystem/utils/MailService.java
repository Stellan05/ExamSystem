package org.example.examsystem.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendSimpleMail(String from, String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from == null ? "" : from);
        message.setTo(to == null ? "" : to);
        message.setSubject(subject == null ? "" : subject);
        message.setText(content == null ? "" : content);
        mailSender.send(message);
    }
}

