package br.com.lasa.notificacao.config.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailConfig {
    /*
    @Value("${spring.mail.host}")
    private String host;JavaMailSender
    @Value("${spring.mail.port}")
    private Integer port;
    @Value("${spring.mail.port}")
    private Integer port;
    */

    @Value("${application.mail.auth.user-name}")
    private String mailAuthUserName;

    @Value("${application.mail.auth.pass-word}")
    private String mailAuthPassword;

    @Value("${application.mail.auth.required}")
    private Boolean mailAuthRequired;

    @Value("${application.mail.host}")
    private String mailHost;

    @Value("${application.mail.ssl.required}")
    private String mailSslRequired;

    private String getProtocol(){
        if (mailSslRequired.equals("true")){
            return "mail.smtps";
        }else {
            return "mail.smtp";
        }
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties props = mailSender.getJavaMailProperties();
        mailSender.setUsername(mailAuthUserName);
        mailSender.setPassword(mailAuthPassword);
        /*mailSender.setHost("smtp.office365.com");
        mailSender.setProtocol("smtp");
        mailSender.setPort(587);*/
        //props.put("mail.smtp.host", "10.23.94.230");
        //props.put("mail.smtp.starttls.required", "true");
        //props.put("mmail.smtp.ehloail.smtp.ehlo", "false");
        props.put(getProtocol()+ ".host", mailHost);
        props.put(getProtocol()+ ".port", 587);
        props.put(getProtocol()+ ".auth", mailAuthRequired);
        props.put(getProtocol()+ ".starttls.enable", mailSslRequired);
        props.put(getProtocol()+ ".ssl.enable", mailSslRequired);
        props.put(getProtocol()+ ".ssl.trust", "*");
        props.put(getProtocol()+ ".debug", "true");
        //props.put("mail.smtp.writetimeout", "1");
        /*try {
            mailSender.testConnection();
        } catch (MessagingException e) {
            e.printStackTrace();
        }*/
        return mailSender;
    }

}
