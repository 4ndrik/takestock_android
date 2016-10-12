package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class Photo implements Parcelable {

    private int mId;
    private String mImage;
    private String mThumbnail;
    private String mThumbnailLarge;
    private boolean mIsMain;
    private int mWidth;
    private int mHeight;

    public Photo(){}

    private Photo(int id,
                  String image,
                  String thumbnail,
                  String thumbnailLarge,
                  boolean isMain,
                  int width,
                  int height) {
        mId = id;
        mImage = image;
        mThumbnail = thumbnail;
        mThumbnailLarge = thumbnailLarge;
        mIsMain = isMain;
        mWidth = width;
        mHeight = height;
    }

    protected Photo(Parcel in) {
        mId = in.readInt();
        mImage = in.readString();
        mThumbnail = in.readString();
        mThumbnailLarge = in.readString();
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

    public String getImage() {
        return mImage;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getThumbnailLarge() {
        return mThumbnailLarge;
    }

    public boolean isMain() {
        return mIsMain;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        if (mId != photo.mId) return false;
        if (mIsMain != photo.mIsMain) return false;
        if (mWidth != photo.mWidth) return false;
        if (mHeight != photo.mHeight) return false;
        if (mImage != null ? !mImage.equals(photo.mImage) : photo.mImage != null) return false;
        if (mThumbnail != null ? !mThumbnail.equals(photo.mThumbnail) : photo.mThumbnail != null) return false;
        return mThumbnailLarge != null ? mThumbnailLarge.equals(photo.mThumbnailLarge) : photo.mThumbnailLarge == null;

    }

    @Override public int hashCode() {
        int result = mId;
        result = 31 * result + (mImage != null ? mImage.hashCode() : 0);
        result = 31 * result + (mThumbnail != null ? mThumbnail.hashCode() : 0);
        result = 31 * result + (mThumbnailLarge != null ? mThumbnailLarge.hashCode() : 0);
        result = 31 * result + (mIsMain ? 1 : 0);
        result = 31 * result + mWidth;
        result = 31 * result + mHeight;
        return result;
    }

    @Override public String toString() {
        return "Photo{" +
                "mId=" + mId +
                ", mImage='" + mImage + '\'' +
                ", mThumbnail='" + mThumbnail + '\'' +
                ", mThumbnailLarge='" + mThumbnailLarge + '\'' +
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
        dest.writeString(mImage);
        dest.writeString(mThumbnail);
        dest.writeString(mThumbnailLarge);
        dest.writeByte((byte) (mIsMain ? 1 : 0));
        dest.writeInt(mWidth);
        dest.writeInt(mHeight);
    }

    public static class Builder {
        private int mId;
        private String mImage;
        private String mThumbnail;
        private String mThumbnailLarge;
        private boolean mIsMain;
        private int mWidth;
        private int mHeight;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setImage(String imagePath) {
            mImage = imagePath;
            return this;
        }

        public Builder setThumbnail(String thumbnail) {
            mThumbnail = thumbnail;
            return this;
        }

        public Builder setThumbnailLarge(String thumbnailLarge) {
            mThumbnailLarge = thumbnailLarge;
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
            return new Photo(mId, mImage, mThumbnail, mThumbnailLarge, mIsMain, mWidth, mHeight);
        }
    }
}
