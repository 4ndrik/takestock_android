package com.devabit.takestock.data.source.remote.jsonModel;

/**
 * Created by Victor Artemyev on 25/10/2016.
 */

public class InviteJson implements JsonModel {

    public final String email;

    public InviteJson(String email) {
        this.email = email;
    }

    public static final class Status implements JsonModel {

        private final static String OK = "ok";

        public String status;
        public String data;

        public boolean isSuccess() {
            return OK.equals(status);
        }
    }
}
