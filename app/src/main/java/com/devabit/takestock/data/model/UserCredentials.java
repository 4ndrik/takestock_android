package com.devabit.takestock.data.model;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public class UserCredentials {

    public final String name;
    public final String password;

    public UserCredentials(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override public String toString() {
        return "UserCredentials{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
