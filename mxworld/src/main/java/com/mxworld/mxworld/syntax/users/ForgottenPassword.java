package com.mxworld.mxworld.syntax.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgottenPassword {
    private String token;
    private String newPassword;

}
