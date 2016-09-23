package com.devabit.takestock.data.model;

import java.util.Arrays;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class Answer {

    private int mId;
    private int mUserId;
    private String mMessage;
    private String mCreatedAt;
    private int[] mQuestion;
    private String mUserName;

    private Answer(){}

    private Answer(int id, int userId, String message, String createdAt, int[] question, String userName) {
        mId = id;
        mUserId = userId;
        mMessage = message;
        mCreatedAt = createdAt;
        mQuestion = question;
        mUserName = userName;
    }

    public int getId() {
        return mId;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public int[] getQuestion() {
        return mQuestion;
    }

    public String getUserName() {
        return mUserName;
    }

    @Override public String toString() {
        return "Answer{" +
                "mId=" + mId +
                ", mUserId=" + mUserId +
                ", mMessage='" + mMessage + '\'' +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                ", mQuestion=" + Arrays.toString(mQuestion) +
                ", mUserName='" + mUserName + '\'' +
                '}';
    }

    public static class Builder {

        private int mId;
        private int mUserId;
        private String mMessage;
        private String mCreatedAt;
        private int[] mQuestion;
        private String mUserName;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setUserId(int userId) {
            mUserId = userId;
            return this;
        }

        public Builder setMessage(String message) {
            mMessage = message;
            return this;
        }

        public Builder setCreatedAt(String createdAt) {
            mCreatedAt = createdAt;
            return this;
        }

        public Builder setQuestion(int[] question) {
            mQuestion = question;
            return this;
        }

        public Builder setUserName(String userName) {
            mUserName = userName;
            return this;
        }

        public Answer create() {
            return new Answer(mId, mUserId, mMessage, mCreatedAt, mQuestion, mUserName);
        }
    }
}
