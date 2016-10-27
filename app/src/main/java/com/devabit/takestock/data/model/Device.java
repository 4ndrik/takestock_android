package com.devabit.takestock.data.model;

/**
 * Created by Victor Artemyev on 19/10/2016.
 */

public class Device {

    private static final String ANDROID = "android";

    private String mId;
    private String mRegistrationId;
    private String mName;
    private String mType;
    private boolean mIsActive;

    private Device(String id, String registrationId, String name, boolean isActive) {
        mId = id;
        mRegistrationId = registrationId;
        mName = name;
        mType = ANDROID;
        mIsActive = isActive;
    }

    public String getId() {
        return mId;
    }

    public void setUserId(int userId) {
        mId = mId + "-" + userId;
    }

    public String getRegistrationId() {
        return mRegistrationId;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public boolean isActive() {
        return mIsActive;
    }

    @Override public String toString() {
        return "Device{" +
                "mId='" + mId + '\'' +
                ", mRegistrationId='" + mRegistrationId + '\'' +
                ", mName='" + mName + '\'' +
                ", mType='" + mType + '\'' +
                ", mIsActive=" + mIsActive +
                '}';
    }

    public static final class Builder {

        private String mId;
        private String mRegistrationId;
        private String mName;
        private boolean mIsActive;

        public Builder setId(String id) {
            mId = id;
            return this;
        }

        public Builder setRegistrationId(String registrationId) {
            mRegistrationId = registrationId;
            return this;
        }

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setIsActive(boolean isActive) {
            mIsActive = isActive;
            return this;
        }

        public Device create() {
            return new Device(mId, mRegistrationId, mName, mIsActive);
        }
    }
}
