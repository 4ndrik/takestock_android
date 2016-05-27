package com.devabit.takestock.data.source.remote.mappers;

import com.devabit.takestock.data.models.UserCredentials;
import org.json.JSONException;
import org.json.JSONObject;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class UserCredentialsJsonMapper implements JsonMapper<UserCredentials> {

    private static final String NAME = "username";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @Override public UserCredentials fromJsonString(String json) throws JSONException {
        throw new UnsupportedOperationException("This operation not required.");
    }

    @Override public String toJsonString(UserCredentials credentials) throws JSONException {
        JSONObject json = new JSONObject();
        if (!isEmpty(credentials.userName)) json.put(NAME, credentials.userName);
        if (!isEmpty(credentials.emailAddress)) json.put(EMAIL, credentials.emailAddress);
        if (!isEmpty(credentials.password)) json.put(PASSWORD, credentials.password);
        return json.toString();
    }
}
