package com.mxworld.mxworld.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Gmail SMTP configuration
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("ramkishore29861@gmail.com");       // <-- replace with your Gmail
        mailSender.setPassword("dyewfhlsqspwwvac");          // <-- replace with App Password (no spaces)

        // Additional mail properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true"); // logs mail activity

        return mailSender;
    }
}
