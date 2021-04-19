package com.cougarneticit.gims.validators;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FieldValidator {

    public static boolean validateEmail(String email) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        return emailValidator.isValid(email);
    }
    public static boolean validateName(String name) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]+");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public static boolean validatePhone(String phone) {
        Pattern pattern = Pattern.compile("^(\\d{3}[-]?){2}\\d{4}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
