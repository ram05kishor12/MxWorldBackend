package com.mxworld.mxworld.syntax.Otp;

public class OtpResponse {
    private int status;
    private String message;
    private String token;

    public OtpResponse(int status, String message , String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int success) {
        this.status = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

     public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
