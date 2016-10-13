package com.devabit.takestock.data.source.remote.jsonModel;

/**
 * Created by Victor Artemyev on 13/10/2016.
 */

public class ChangePasswordJson implements JsonModel {

    private String current_password;
    private String new_password;
    private String status;

    public ChangePasswordJson(String currentPass, String newPass) {
        this.current_password = currentPass;
        this.new_password = newPass;
    }

    public boolean isSuccessful() {
    return status != null && status.equals(Status.OK);
    }

    static class Status {
        static final String OK = "ok";
    }
}
