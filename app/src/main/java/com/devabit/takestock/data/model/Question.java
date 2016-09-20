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
    private String mCreatedAt;
    private String mUserName;
    private Answer mAnswer;

    protected Question(int id, int userId, int advertId, String message, boolean isNew, String createdAt, String userName, Answer answer) {
        mId = id;
        mUserId = userId;
        mAdvertId = advertId;
        mMessage = message;
        mIsNew = isNew;
        mCreatedAt = createdAt;
        mUserName = userName;
        mAnswer = answer;
    }

    protected Question(Parcel in) {
        mId = in.readInt();
        mUserId = in.readInt();
        mAdvertId = in.readInt();
        mMessage = in.readString();
        mIsNew = in.readByte() != 0;
        mCreatedAt = in.readString();
        mUserName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mUserId);
        dest.writeInt(mAdvertId);
        dest.writeString(mMessage);
        dest.writeByte((byte) (mIsNew ? 1 : 0));
        dest.writeString(mCreatedAt);
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

    public int getUserId() {
        return mUserId;
    }

    public int getAdvertId() {
        return mAdvertId;
    }

    public String getMessage() {
        return mMessage;
    }

    public boolean isNew() {
        return mIsNew;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public String getUserName() {
        return mUserName;
    }

    public Answer getAnswer() {
        return mAnswer;
    }

    @Override public String toString() {
        return "Question{" +
                "mId=" + mId +
                ", mUserId=" + mUserId +
                ", mAdvertId=" + mAdvertId +
                ", mMessage='" + mMessage + '\'' +
                ", mIsNew=" + mIsNew +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                ", mUserName='" + mUserName + '\'' +
                ", mAnswer=" + mAnswer +
                '}';
    }

    public static class Builder {

        private int mId;
        private int mUserId;
        private int mAdvertId;
        private String mMessage;
        private boolean mIsNew;
        private String mCreatedAt;
        private String mUserName;
        private Answer mAnswer;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setUserId(int userId) {
            mUserId = userId;
            return this;
        }

        public Builder setAdvertId(int advertId) {
            mAdvertId = advertId;
            return this;
        }

        public Builder setMessage(String message) {
            mMessage = message;
            return this;
        }

        public Builder setIsNew(boolean isNew) {
            mIsNew = isNew;
            return this;
        }

        public Builder setCreatedAt(String dateCreated) {
            mCreatedAt = dateCreated;
            return this;
        }

        public Builder setUserName(String userName) {
            mUserName = userName;
            return this;
        }

        public Builder setAnswer(Answer answer) {
            mAnswer = answer;
            return this;
        }

        public Question create() {
            return new Question(mId, mUserId, mAdvertId, mMessage, mIsNew, mCreatedAt, mUserName, mAnswer);
        }
    }
}
