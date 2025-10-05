package com.mxworld.mxworld.interfaces;

public interface OtpInterface {

    String generateAndSend(String email);

    boolean validateOtp(String token, String otp);

    void sendOtpEmail(String to, String otp);

    boolean updatePassword(String token, String newPassword);
}
