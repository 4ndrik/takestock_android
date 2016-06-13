package com.devabit.takestock.utils;

import java.util.regex.Pattern;

/**
 * Created by Victor Artemyev on 06/05/2016.
 */
public final class Validator {

    public static final Pattern USER_NAME_PATTERN = Pattern.compile("^[A-Za-z0-9\\.\\+\\-_@]{3,30}$");

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
            );

    public static boolean validateUserName(String userName) {
        return USER_NAME_PATTERN.matcher(userName).matches();
    }

    public static boolean validateEmailAddress(String emailAddress) {
        return EMAIL_ADDRESS_PATTERN.matcher(emailAddress).matches();
    }

    private Validator() {
    }
}
