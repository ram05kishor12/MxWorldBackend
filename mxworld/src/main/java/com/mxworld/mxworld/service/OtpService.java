package com.mxworld.mxworld.service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.mxworld.mxworld.model.Otp;
import com.mxworld.mxworld.model.User;
import com.mxworld.mxworld.repository.OtpRepository;
import com.mxworld.mxworld.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class OtpService {

    private final UserService userService;

    private final UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private JavaMailSender mailSender;

    OtpService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

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

        storedOtp.setIsVerified(true);
        otpRepository.save(storedOtp);
        System.out.println("OTP validated for token: " + token + storedOtp.getIsVerified());
        return true;
    }

    @Transactional
    public boolean updatePassword(String token, String newPassword) {
        // 1. Find OTP record by token
        Optional<Otp> storedOtpOpt = otpRepository.findByToken(token);

        if (token == null || token.trim().isEmpty()) {
            System.out.println("Token is null or empty");
            return false;
        }

        if (storedOtpOpt.isEmpty()) {
            System.out.println("Invalid token: " + token);
            return false;
        }

        Otp storedOtp = storedOtpOpt.get();

        // 2. Check if OTP was verified
        if (!Boolean.TRUE.equals(storedOtp.getIsVerified())) {
            System.out.println("OTP not verified for token: " + token);
            return false;
        }

        // 3. Fetch user
        Optional<User> userOpt = userRepository.findByEmail(storedOtp.getEmail());
        if (userOpt.isEmpty()) {
            System.out.println("No user found for token: " + token);
            return false;
        }

        User user = userOpt.get();

        if (userService.checkPassword(user, newPassword)) {
            throw new IllegalArgumentException("New password cannot be same as old password");
        }

        // 4. Update password
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // 5. Delete OTP record
        otpRepository.deleteByToken(token);

        System.out.println("Password updated successfully for user: " + user.getEmail());
        return true;
    }

}
