package com.devabit.takestock.data.model;

import java.util.Arrays;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class Answer {

    private int mId;
    private int mUserId;
    private String mMessage;
    private String mDateCreated;
    private int[] mQuestionSet;
    private String mUserName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public int[] getQuestionSet() {
        return mQuestionSet;
    }

    public void setQuestionSet(int[] questionSet) {
        mQuestionSet = questionSet;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    @Override public String toString() {
        return "Answer{" +
                "mId=" + mId +
                ", mUserId=" + mUserId +
                ", mMessage='" + mMessage + '\'' +
                ", mDateCreated='" + mDateCreated + '\'' +
                ", mQuestionSet=" + Arrays.toString(mQuestionSet) +
                ", mUserName='" + mUserName + '\'' +
                '}';
    }
}
