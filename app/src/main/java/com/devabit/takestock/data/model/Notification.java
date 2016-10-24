package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor Artemyev on 20/10/2016.
 */

public class Notification implements Parcelable{

    private int mId;
    private String mType;
    private String mTitle;
    private String mBody;
    private int mOfferId;
    private int mAdvertId;
    private long mSentTime;
    private boolean mIsNew;

    public static final class Type {
        public static final String OFFER = "offer";
    }

    private Notification(int id, String type, String title, String body, int offerId, int advertId, long sentTime, boolean isNew) {
        mId = id;
        mType = type;
        mTitle = title;
        mBody = body;
        mOfferId = offerId;
        mAdvertId = advertId;
        mSentTime = sentTime;
        mIsNew = isNew;
    }

    protected Notification(Parcel in) {
        mId = in.readInt();
        mType = in.readString();
        mTitle = in.readString();
        mBody = in.readString();
        mOfferId = in.readInt();
        mAdvertId = in.readInt();
        mSentTime = in.readLong();
        mIsNew = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mType);
        dest.writeString(mTitle);
        dest.writeString(mBody);
        dest.writeInt(mOfferId);
        dest.writeInt(mAdvertId);
        dest.writeLong(mSentTime);
        dest.writeByte((byte) (mIsNew ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public int getId() {
        return mId;
    }

    public String getType() {
        return mType;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBody() {
        return mBody;
    }

    public int getOfferId() {
        return mOfferId;
    }

    public int getAdvertId() {
        return mAdvertId;
    }

    public long getSentTime() {
        return mSentTime;
    }

    public void setNew(boolean aNew) {
        mIsNew = aNew;
    }

    public boolean isNew() {
        return mIsNew;
    }

    @Override public String toString() {
        return "Notification{" +
                "mId=" + mId +
                ", mType='" + mType + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mBody='" + mBody + '\'' +
                ", mOfferId=" + mOfferId +
                ", mAdvertId=" + mAdvertId +
                ", mSentTime=" + mSentTime +
                ", mIsNew=" + mIsNew +
                '}';
    }

    public static final class Builder {

        private int mId;
        private String mType;
        private String mTitle;
        private String mBody;
        private int mOfferId;
        private int mAdvertId;
        private long mSentTime;
        private boolean mIsNew;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setType(String type) {
            mType = type;
            return this;
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setBody(String body) {
            mBody = body;
            return this;
        }

        public Builder setOfferId(int offerId) {
            mOfferId = offerId;
            return this;
        }

        public Builder setAdvertId(int advertId) {
            mAdvertId = advertId;
            return this;
        }

        public Builder setSentTime(long sentTime) {
            mSentTime = sentTime;
            return this;
        }

        public Builder setNew(boolean aNew) {
            mIsNew = aNew;
            return this;
        }

        public Notification create() {
            return new Notification(mId, mType, mTitle, mBody, mOfferId, mAdvertId, mSentTime, mIsNew);
        }
    }
}
