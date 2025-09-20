package com.mxworld.mxworld.utility;

public class UserFuncion {

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8)
            return false;

        // Regex: at least one uppercase, one number, one special character
        String pattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(pattern);
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty())
            return false;

        // Regex for email validation
        String pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email.matches(pattern);
    }
}
