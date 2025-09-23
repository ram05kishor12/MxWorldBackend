package com.mxworld.mxworld.syntax.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgottenPasswordResponse {
    private int status;
    private String message;

    public ForgottenPasswordResponse(int status , String message) {
        this.status = status;
        this.message = message;
    }
}
