package com.mxworld.mxworld.syntax.Otp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpValidationResponse {
    private int status;
    private String message;

    public OtpValidationResponse(int status , String message) {
        this.status = status;
        this.message = message;
    }
}
