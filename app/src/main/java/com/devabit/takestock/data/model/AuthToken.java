package com.devabit.takestock.data.model;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public class AuthToken {

    public int userId;
    public String username;
    public String email;
    public String token;

    @Override public String toString() {
        return "AuthToken{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
