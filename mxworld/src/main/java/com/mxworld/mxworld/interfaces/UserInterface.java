package com.mxworld.mxworld.interfaces;

import java.util.Optional;

import com.mxworld.mxworld.model.User;


public interface UserInterface {

    User register(User user);

    Optional<User> login(String email, String password);

    boolean checkPassword(User user, String rawPassword);

}
