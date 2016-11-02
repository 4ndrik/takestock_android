package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by Victor Artemyev on 20/10/2016.
 */

public class Notification implements Parcelable{

    public static final class Action {
        public static final String MAIN = "com.takestock.action.MAIN";
        public static final String SELLING = "com.takestock.action.SELLING";
        public static final String BUYING ="com.takestock.action.BUYING";
        public static final String ADVERT_QUESTION = "com.takestock.action.ADVERT_QUESTION";
        public static final String ADVERT_ANSWER = "com.takestock.action.ADVERT_ANSWER";
    }

    public static final String EXTRA_ACTION = "action";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_BODY = "body";
    public static final String EXTRA_ADVERT_ID = "advert_id";
    public static final String EXTRA_OFFER_ID = "offer_id";
    public static final String EXTRA_MESSAGE = "msg";

    private int mId;
    private String mAction;
    private String mTitle;
    private String mBody;
    private int mOfferId;
    private int mAdvertId;
    private String mMessage;
    private long mSentTime;
    private boolean mIsSaved;

    private Notification(int id,
                         String action,
                         String title,
                         String body,
                         int offerId,
                         int advertId,
                         String message,
                         long sentTime,
                         boolean isSaved) {
        mId = id;
        mAction = action;
        mTitle = title;
        mBody = body;
        mOfferId = offerId;
        mAdvertId = advertId;
        mMessage = message;
        mSentTime = sentTime;
        mIsSaved = isSaved;
    }

    protected Notification(Parcel in) {
        mId = in.readInt();
        mAction = in.readString();
        mTitle = in.readString();
        mBody = in.readString();
        mOfferId = in.readInt();
        mAdvertId = in.readInt();
        mMessage = in.readString();
        mSentTime = in.readLong();
        mIsSaved = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mAction);
        dest.writeString(mTitle);
        dest.writeString(mBody);
        dest.writeInt(mOfferId);
        dest.writeInt(mAdvertId);
        dest.writeString(mMessage);
        dest.writeLong(mSentTime);
        dest.writeByte((byte) (mIsSaved ? 1 : 0));
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

    public String getAction() {
        return mAction;
    }

    public boolean hasAction() {
        return mAction != null && !mAction.isEmpty();
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

    public String getMessage() {
        return mMessage;
    }

    public long getSentTime() {
        return mSentTime;
    }

    public void setSaved(boolean saved) {
        mIsSaved = saved;
    }

    public boolean isSaved() {
        return mIsSaved;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (mId != that.mId) return false;
        if (mOfferId != that.mOfferId) return false;
        if (mAdvertId != that.mAdvertId) return false;
        if (mSentTime != that.mSentTime) return false;
        if (mIsSaved != that.mIsSaved) return false;
        if (mAction != null ? !mAction.equals(that.mAction) : that.mAction != null) return false;
        if (mTitle != null ? !mTitle.equals(that.mTitle) : that.mTitle != null) return false;
        if (mBody != null ? !mBody.equals(that.mBody) : that.mBody != null) return false;
        return mMessage != null ? mMessage.equals(that.mMessage) : that.mMessage == null;

    }

    @Override public int hashCode() {
        int result = mId;
        result = 31 * result + (mAction != null ? mAction.hashCode() : 0);
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mBody != null ? mBody.hashCode() : 0);
        result = 31 * result + mOfferId;
        result = 31 * result + mAdvertId;
        result = 31 * result + (mMessage != null ? mMessage.hashCode() : 0);
        result = 31 * result + (int) (mSentTime ^ (mSentTime >>> 32));
        result = 31 * result + (mIsSaved ? 1 : 0);
        return result;
    }

    @Override public String toString() {
        return "Notification{" +
                "mId=" + mId +
                ", mAction='" + mAction + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mBody='" + mBody + '\'' +
                ", mOfferId=" + mOfferId +
                ", mAdvertId=" + mAdvertId +
                ", mMessage='" + mMessage + '\'' +
                ", mSentTime=" + mSentTime +
                ", mIsSaved=" + mIsSaved +
                '}';
    }

    public static final class Builder {

        private int mId;
        private String mAction;
        private String mTitle;
        private String mBody;
        private int mOfferId;
        private int mAdvertId;
        private String mMessage;
        private long mSentTime;
        private boolean mIsSaved;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setAction(@Nullable String action) {
            mAction = action == null ? "" : action;
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

        public Builder setMessage(String message) {
            mMessage = message;
            return this;
        }

        public Builder setSentTime(long sentTime) {
            mSentTime = sentTime;
            return this;
        }

        public Builder setSaved(boolean saved) {
            mIsSaved = saved;
            return this;
        }

        public Notification create() {
            return new Notification(mId, mAction, mTitle, mBody, mOfferId, mAdvertId, mMessage, mSentTime, mIsSaved);
        }
    }
}
