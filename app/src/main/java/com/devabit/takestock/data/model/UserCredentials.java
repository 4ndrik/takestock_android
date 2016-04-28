package com.devabit.takestock.data.model;

import com.devabit.takestock.data.source.remote.RequestObject;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public final class UserCredentials implements RequestObject {

    private static final String NAME = "username";
    private static final String PASSWORD = "password";

    public final String name;
    public final String password;

    public UserCredentials(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override public String toJsonString() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(NAME, name);
        json.put(PASSWORD, password);
        return json.toString();
    }

    @Override public String toString() {
        return "UserCredentials{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
