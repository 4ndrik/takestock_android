package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class Question implements Parcelable {

    private int mId;
    private int mUserId;
    private int mAdvertId;
    private String mMessage;
    private boolean mIsNew;
    private String mDateCreated;
    private String mUserName;
    private Answer mAnswer;

    public Question() {}

    protected Question(Parcel in) {
        mId = in.readInt();
        mUserId = in.readInt();
        mAdvertId = in.readInt();
        mMessage = in.readString();
        mIsNew = in.readByte() != 0;
        mDateCreated = in.readString();
        mUserName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mUserId);
        dest.writeInt(mAdvertId);
        dest.writeString(mMessage);
        dest.writeByte((byte) (mIsNew ? 1 : 0));
        dest.writeString(mDateCreated);
        dest.writeString(mUserName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

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

    public Answer getAnswer() {
        return mAnswer;
    }

    public void setAnswer(Answer answer) {
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
