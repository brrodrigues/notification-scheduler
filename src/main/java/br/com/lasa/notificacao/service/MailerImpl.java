package br.com.lasa.notificacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MailerImpl implements Mailer {

    @Autowired private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String sendTo, String subject, String message) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sendTo);
        simpleMailMessage.setTo(sendTo);
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setText(message);
        simpleMailMessage.setSubject(subject);

        javaMailSender.send(simpleMailMessage);
    }
}
