package com.devabit.takestock.data.source.remote.mapper;

import com.devabit.takestock.data.model.UserCredentials;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class UserCredentialsMapper implements ToJsonMapper<UserCredentials> {

    private static final String NAME = "username";
    private static final String PASSWORD = "password";

    @Override public String toJsonString(UserCredentials userCredentials) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(NAME, userCredentials.name);
        json.put(PASSWORD, userCredentials.password);
        return json.toString();
    }
}
