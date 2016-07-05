package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class OfferStatus implements Parcelable {

    public static final int ACCEPTED = 1;
    public static final int REJECTED = 2;
    public static final int PENDING = 3;
    public static final int COUNTERED = 4;
    public static final int PAYMENT_MADE = 5;

    @IntDef({ACCEPTED, REJECTED, PENDING, COUNTERED, PAYMENT_MADE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {}

    private int mId;
    private String mType;

    public OfferStatus(){}

    protected OfferStatus(Parcel in) {
        mId = in.readInt();
        mType = in.readString();
    }

    public static final Creator<OfferStatus> CREATOR = new Creator<OfferStatus>() {
        @Override
        public OfferStatus createFromParcel(Parcel in) {
            return new OfferStatus(in);
        }

        @Override
        public OfferStatus[] newArray(int size) {
            return new OfferStatus[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    @Override public String toString() {
        return "OfferStatus{" +
                "mId=" + mId +
                ", mType='" + mType + '\'' +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mType);
    }
}
