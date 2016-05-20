package com.devabit.takestock.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class User implements Parcelable {

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
    private Photo mPhoto;

    public User(){}

    protected User(Parcel in) {
        mId = in.readInt();
        mUserName = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mEmail = in.readString();
        mDateJoined = in.readString();
        mDateLastLogin = in.readString();
        mIsSuperuser = in.readByte() != 0;
        mIsStaff = in.readByte() != 0;
        mIsActive = in.readByte() != 0;
        mIsSubscribed = in.readByte() != 0;
        mIsVerified = in.readByte() != 0;
        mIsVatExempt = in.readByte() != 0;
        mAvgRating = in.readDouble();
        mPhoto = in.readParcelable(Photo.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }

    @Override public String toString() {
        return "Author{" +
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
                ", mPhoto=" + mPhoto +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mUserName);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mEmail);
        dest.writeString(mDateJoined);
        dest.writeString(mDateLastLogin);
        dest.writeByte((byte) (mIsSuperuser ? 1 : 0));
        dest.writeByte((byte) (mIsStaff ? 1 : 0));
        dest.writeByte((byte) (mIsActive ? 1 : 0));
        dest.writeByte((byte) (mIsSubscribed ? 1 : 0));
        dest.writeByte((byte) (mIsVerified ? 1 : 0));
        dest.writeByte((byte) (mIsVatExempt ? 1 : 0));
        dest.writeDouble(mAvgRating);
        dest.writeParcelable(mPhoto, flags);
    }
}
