package com.mxworld.mxworld.syntax.Profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileResponse {
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profileUrl;
    private String descString;
}
