package com.devabit.takestock.data.model;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public class AuthToken {

    private int mUserId;
    private String mUsername;
    private String mEmail;
    private String mToken;
    private User mUser;

    private AuthToken(int userId, String username, String email, String token, User user) {
        mUserId = userId;
        mUsername = username;
        mEmail = email;
        mToken = token;
        mUser = user;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getToken() {
        return mToken;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public User getUser() {
        return mUser;
    }

    @Override public String toString() {
        return "AuthToken{" +
                "mUserId=" + mUserId +
                ", mUsername='" + mUsername + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mToken='" + mToken + '\'' +
                ", mUser=" + mUser +
                '}';
    }

    public static final class Builder {

        private int mUserId;
        private String mUsername;
        private String mEmail;
        private String mToken;
        private User mUser;

        public Builder setUserId(int userId) {
            mUserId = userId;
            return this;
        }

        public Builder setUsername(String username) {
            mUsername = username;
            return this;
        }

        public Builder setEmail(String email) {
            mEmail = email;
            return this;
        }

        public Builder setToken(String token) {
            mToken = token;
            return this;
        }

        public Builder setUser(User user) {
            mUser = user;
            return this;
        }

        public AuthToken create() {
            return new AuthToken(mUserId, mUsername, mEmail, mToken, mUser);
        }
    }
}
