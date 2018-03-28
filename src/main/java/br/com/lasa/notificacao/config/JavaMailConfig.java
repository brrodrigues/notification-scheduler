package br.com.lasa.notificacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailConfig {
    /*
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private Integer port;
    @Value("${spring.mail.port}")
    private Integer port;
    */
    @Bean(name = "javaMailSender")
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties props = mailSender.getJavaMailProperties();
        mailSender.setUsername("bcrodrigues");
        mailSender.setPassword("_+Br9n7_+");
        /*mailSender.setHost("smtp.office365.com");
        mailSender.setProtocol("smtp");
        mailSender.setPort(587);*/
        //props.put("mail.smtp.host", "10.23.94.230");
        //props.put("mail.smtp.starttls.required", "true");
        //props.put("mail.smtp.ehlo", "false");
        //props.put("mail.smtp.auth.login.disable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", "false");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtps.ssl.trust", "*");
        props.put("mail.smtp.ssl.enable", "false");
        //props.put("mail.smtp.writetimeout", "1");
        /*try {
            mailSender.testConnection();
        } catch (MessagingException e) {
            e.printStackTrace();
        }*/
        return mailSender;
    }

}
