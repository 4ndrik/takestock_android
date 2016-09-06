package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class Photo implements Parcelable {

    private int mId;
    private String mImagePath;
    private boolean mIsMain;
    private int mWidth;
    private int mHeight;

    public Photo(){}

    Photo(int id, String imagePath, boolean isMain, int width, int height) {
        mId = id;
        mImagePath = imagePath;
        mIsMain = isMain;
        mWidth = width;
        mHeight = height;
    }

    protected Photo(Parcel in) {
        mId = in.readInt();
        mImagePath = in.readString();
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

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
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
                ", mImagePath='" + mImagePath + '\'' +
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
        dest.writeString(mImagePath);
        dest.writeByte((byte) (mIsMain ? 1 : 0));
        dest.writeInt(mWidth);
        dest.writeInt(mHeight);
    }

    public static class Builder {
        private int mId;
        private String mImagePath;
        private boolean mIsMain;
        private int mWidth;
        private int mHeight;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setImage(String imagePath) {
            mImagePath = imagePath;
            return this;
        }

        public Builder setIsMain(boolean isMain) {
            mIsMain = isMain;
            return this;
        }

        public Builder setWidth(int width) {
            mWidth = width;
            return this;
        }

        public Builder setHeight(int height) {
            mHeight = height;
            return this;
        }

        public Photo build() {
            return new Photo(mId, mImagePath, mIsMain, mWidth, mHeight);
        }
    }
}
