package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class QuestionEntity extends RealmObject {

    @PrimaryKey
    private int mId;
    private int mUserId;
    private int mAdvertId;
    private String mMessage;
    private boolean mIsNew;
    private String mDateCreated;
    private String mUserName;
    private AnswerEntity mAnswer;

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

    public int getAdvertId() {
        return mAdvertId;
    }

    public void setAdvertId(int advertId) {
        mAdvertId = advertId;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public boolean isNew() {
        return mIsNew;
    }

    public void setNew(boolean aNew) {
        mIsNew = aNew;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public AnswerEntity getAnswer() {
        return mAnswer;
    }

    public void setAnswer(AnswerEntity answer) {
        mAnswer = answer;
    }

    @Override public String toString() {
        return "Question{" +
                "mId=" + mId +
                ", mUserId=" + mUserId +
                ", mAdvertId=" + mAdvertId +
                ", mMessage='" + mMessage + '\'' +
                ", mIsNew=" + mIsNew +
                ", mDateCreated='" + mDateCreated + '\'' +
                ", mUserName='" + mUserName + '\'' +
                ", mAnswer=" + mAnswer +
                '}';
    }
}
