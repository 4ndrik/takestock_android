package com.devabit.takestock.data.model;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public class UserCredentials {

    public String userName;
    public String emailAddress;
    public String password;

    @Override public String toString() {
        return "UserCredentials{" +
                "userName='" + userName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
