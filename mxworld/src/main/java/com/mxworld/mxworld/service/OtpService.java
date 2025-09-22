package com.mxworld.mxworld.service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.mxworld.mxworld.model.Otp;
import com.mxworld.mxworld.repository.OtpRepository;

import jakarta.transaction.Transactional;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ramkishore29861@gmail.com");
        message.setTo(to);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp + "\nThis OTP is valid for 5 minutes.");

        mailSender.send(message);
    }

    public String generateAndSend(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        String token = UUID.randomUUID().toString();

        Otp otpdb = new Otp();
        otpdb.setEmail(email);
        otpdb.setOtp(otp);
        otpdb.setToken(token);
        otpdb.setExDateTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(otpdb);
        sendOtpEmail(email, otp);
        return token;
    }

    @Transactional
    public boolean validateOtp(String token, String otp) {
        // Fetch OTP record by token
        Optional<Otp> storedOtpOpt = otpRepository.findByToken(token);

        if (storedOtpOpt.isEmpty()) {
            System.out.println("No OTP found for token: " + token);
            return false; // token doesn't exist
        }

        Otp storedOtp = storedOtpOpt.get();

        // Check if OTP matches and is not expired
        if (!storedOtp.getOtp().equals(otp)) {
            System.out.println(
                    "OTP mismatch for token: " + token + ". Expected: " + storedOtp.getOtp() + ", Provided: " + otp);
            return false;
        }

        if (storedOtp.getExDateTime().isBefore(LocalDateTime.now())) {
            System.out.println("OTP expired for token: " + token + ". Expiry: " + storedOtp.getExDateTime());
            return false;
        }

        // Valid OTP, delete record
        otpRepository.deleteByToken(token);
        System.out.println("OTP validated and deleted for token: " + token);
        return true;
    }

}
