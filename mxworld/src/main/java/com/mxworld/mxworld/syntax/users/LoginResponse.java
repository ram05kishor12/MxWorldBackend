package com.mxworld.mxworld.syntax.users;

public class LoginResponse {
    private int status;
    private String message;
    private String jwtToken;
    private String refreshToken;

    public LoginResponse(int status, String message , String jwtToken , String refreshToken) {
        this.status = status;
        this.message = message;
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
    }

    // Getters and setters
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

    public String getJwtToken(){
        return jwtToken;
    }

    public void setJwtToken (String jwt){
        this.jwtToken = jwt;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

    public void setRefreshToken (String refreshToken){
        this.jwtToken = refreshToken;
    }
}
