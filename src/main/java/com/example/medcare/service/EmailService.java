package com.example.medcare.service;


import com.example.medcare.dto.MailBody;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void setSimpleMessage(MailBody mailBody){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.to());
        message.setFrom("ebrahim.alaa26@gmail.com");
        message.setSubject(mailBody.subject());
        message.setText(mailBody.body());

        javaMailSender.send(message);



    }


}
