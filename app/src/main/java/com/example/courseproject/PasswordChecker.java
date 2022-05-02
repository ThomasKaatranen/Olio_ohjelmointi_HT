package com.example.courseproject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordChecker {

    private static PasswordChecker pc = null;

    public static PasswordChecker getInstance() {
        if (pc == null) {
            pc = new PasswordChecker();
        }
        return pc;
    }

    //https://whaa.dev/how-to-check-if-a-string-contains-uppercase-in-java
    public boolean checkPasswordContainsUpperCaseCharacter(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    //https://whaa.dev/how-to-check-if-a-string-contains-uppercase-in-java
    public boolean checkPasswordContainsLowerCaseCharacter(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    //https://stackoverflow.com/questions/1795402/check-if-a-string-contains-a-special-character
    public boolean checkPasswordContainsSpecialCharacter(String password) {
        Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        Matcher hasSpecial = special.matcher(password);
        boolean containsSymbol = hasSpecial.find();
        if (containsSymbol) {
            return true;
        }
        return false;
    }

    //https://stackoverflow.com/questions/1795402/check-if-a-string-contains-a-special-character
    public boolean checkPasswordContainsNumber(String password) {
        Pattern number = Pattern.compile("[0-9]");

        Matcher hasNumber = number.matcher(password);
        boolean containsNumber = hasNumber.find();
        if (containsNumber) {
            return true;
        }
        return false;
    }

}
