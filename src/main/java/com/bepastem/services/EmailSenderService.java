package com.bepastem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import java.sql.Date;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body){
       try{
           SimpleMailMessage message = new SimpleMailMessage();

           message.setFrom("o4crypto@gmail.com");
           message.setTo(toEmail);
           message.setReplyTo("mrchimeudeonwo@gmail.com");
           message.setSubject(subject);
           message.setText(body);
           message.setSentDate(new Date(0));

           mailSender.send(message);

           System.out.println("Email sent successfully! to " + toEmail);
       } catch (Exception e){
           e.printStackTrace();
       }
    }

    /***/
    /*public void sendMessageWithAttachment(
  String to, String subject, String text, String pathToAttachment) {
    // ...

    MimeMessage message = emailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setFrom("noreply@baeldung.com");
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(text);

    FileSystemResource file
      = new FileSystemResource(new File(pathToAttachment));
    helper.addAttachment("Invoice", file);

    emailSender.send(message);
    // ...
}*/
}
