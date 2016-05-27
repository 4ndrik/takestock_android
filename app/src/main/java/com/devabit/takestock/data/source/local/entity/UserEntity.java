package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class UserEntity extends RealmObject {

    @PrimaryKey
    private int mId;
    private String mUserName;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mDateJoined;
    private String mDateLastLogin;
    private boolean mIsSuperuser;
    private boolean mIsStaff;
    private boolean mIsActive;
    private boolean mIsSubscribed;
    private boolean mIsVerified;
    private boolean mIsVatExempt;
    private double mAvgRating;
    private PhotoEntity mPhotoEntity;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getDateJoined() {
        return mDateJoined;
    }

    public void setDateJoined(String dateJoined) {
        mDateJoined = dateJoined;
    }

    public String getDateLastLogin() {
        return mDateLastLogin;
    }

    public void setDateLastLogin(String dateLastLogin) {
        mDateLastLogin = dateLastLogin;
    }

    public boolean isSuperuser() {
        return mIsSuperuser;
    }

    public void setSuperuser(boolean superuser) {
        mIsSuperuser = superuser;
    }

    public boolean isStaff() {
        return mIsStaff;
    }

    public void setStaff(boolean staff) {
        mIsStaff = staff;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean active) {
        mIsActive = active;
    }

    public boolean isSubscribed() {
        return mIsSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        mIsSubscribed = subscribed;
    }

    public boolean isVerified() {
        return mIsVerified;
    }

    public void setVerified(boolean verified) {
        mIsVerified = verified;
    }

    public boolean isVatExempt() {
        return mIsVatExempt;
    }

    public void setVatExempt(boolean vatExempt) {
        mIsVatExempt = vatExempt;
    }

    public double getAvgRating() {
        return mAvgRating;
    }

    public void setAvgRating(double avgRating) {
        mAvgRating = avgRating;
    }

    public PhotoEntity getPhotoEntity() {
        return mPhotoEntity;
    }

    public void setPhotoEntity(PhotoEntity photoEntity) {
        mPhotoEntity = photoEntity;
    }

    @Override public String toString() {
        return "UserEntity{" +
                "mId=" + mId +
                ", mUserName='" + mUserName + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mDateJoined='" + mDateJoined + '\'' +
                ", mDateLastLogin='" + mDateLastLogin + '\'' +
                ", mIsSuperuser=" + mIsSuperuser +
                ", mIsStaff=" + mIsStaff +
                ", mIsActive=" + mIsActive +
                ", mIsSubscribed=" + mIsSubscribed +
                ", mIsVerified=" + mIsVerified +
                ", mIsVatExempt=" + mIsVatExempt +
                ", mAvgRating=" + mAvgRating +
                ", mPhotoEntity=" + mPhotoEntity +
                '}';
    }
}
