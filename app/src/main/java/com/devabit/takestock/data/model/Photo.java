package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class Photo implements Parcelable {

    private int mId;
    private String mImageUrl;
    private boolean mIsMain;
    private int mWidth;
    private int mHeight;

    public Photo(){}

    protected Photo(Parcel in) {
        mId = in.readInt();
        mImageUrl = in.readString();
        mIsMain = in.readByte() != 0;
        mWidth = in.readInt();
        mHeight = in.readInt();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public boolean isMain() {
        return mIsMain;
    }

    public void setMain(boolean main) {
        mIsMain = main;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    @Override public String toString() {
        return "Photo{" +
                "mId=" + mId +
                ", mImageUrl='" + mImageUrl + '\'' +
                ", mIsMain=" + mIsMain +
                ", mWidth=" + mWidth +
                ", mHeight=" + mHeight +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mImageUrl);
        dest.writeByte((byte) (mIsMain ? 1 : 0));
        dest.writeInt(mWidth);
        dest.writeInt(mHeight);
    }
}
