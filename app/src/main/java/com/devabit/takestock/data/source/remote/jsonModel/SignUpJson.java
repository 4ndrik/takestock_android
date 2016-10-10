package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.UserCredentials;

/**
 * Created by Victor Artemyev on 09/10/2016.
 */

public class SignUpJson implements JsonModel {
    public String email;
    public String username;
    public String password;

    public SignUpJson(UserCredentials credentials) {
        this.email = credentials.emailAddress;
        this.username = credentials.userName;
        this.password = credentials.password;
    }
}
