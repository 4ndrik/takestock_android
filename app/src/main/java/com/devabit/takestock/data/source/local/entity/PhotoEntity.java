package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class PhotoEntity extends RealmObject {

    @PrimaryKey
    private int mId;
    private String mImagePath;
    private boolean mIsMain;
    private int mWidth;
    private int mHeight;

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
        return "PhotoEntity{" +
                "mId=" + mId +
                ", mImagePath='" + mImagePath + '\'' +
                ", mIsMain=" + mIsMain +
                ", mWidth=" + mWidth +
                ", mHeight=" + mHeight +
                '}';
    }
}
