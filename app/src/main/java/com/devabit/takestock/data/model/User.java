package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor Artemyev on 06/09/2016.
 */
public class User implements Parcelable {

    private int mId;
    private String mUserName;
    private String mFirstName;
    private String mLastName;
    private String mBusinessTypeName;
    private String mBusinessSubtypeName;
    private String mLastLogin;
    private boolean mIsSuperuser;
    private int mRole;
    private String mPartnerParent;
    private String mPartner;
    private String mRememberToken;
    private String mPaymentMethod;
    private String mAccountNumber;
    private String mService;
    private String mGroupReferences;
    private String mGroupCode;
    private boolean mIsSeller;
    private String mDescription;
    private String mOldId;
    private String mEmail;
    private boolean mIsStaff;
    private boolean mIsActive;
    private String mDateJoined;
    private boolean mIsSubscribed;
    private boolean mIsVerified;
    private boolean mIsVatExempt;
    private double mAvgRating;
    private String mPhoto;
    private String mBusinessName;
    private String mPostcode;
    private String mVatNumber;
    private boolean mHasNotifications;
    private int mBusinessTypeId;
    private int mBusinessSubtypeId;
    private boolean mIsVerifiedByStaffMember;
    //            "stripe_id": null
    //            "last4": null

    private User(int id, String userName, String firstName, String lastName, String businessTypeName,
                 String businessSubtypeName, String lastLogin, boolean isSuperuser, int role, String partnerParent,
                 String partner, String rememberToken, String paymentMethod, String accountNumber, String service,
                 String groupReferences, String groupCode, boolean isSeller, String description, String oldId,
                 String email, boolean isStaff, boolean isActive, String dateJoined, boolean isSubscribed,
                 boolean isVerified, boolean isVatExempt, double avgRating, String photo, String businessName,
                 String postcode, String vatNumber, boolean hasNotifications, int businessTypeId, int businessSubtypeId,
                 boolean isVerifiedByStaffMember) {
        mId = id;
        mUserName = userName;
        mFirstName = firstName;
        mLastName = lastName;
        mBusinessTypeName = businessTypeName;
        mBusinessSubtypeName = businessSubtypeName;
        mLastLogin = lastLogin;
        mIsSuperuser = isSuperuser;
        mRole = role;
        mPartnerParent = partnerParent;
        mPartner = partner;
        mRememberToken = rememberToken;
        mPaymentMethod = paymentMethod;
        mAccountNumber = accountNumber;
        mService = service;
        mGroupReferences = groupReferences;
        mGroupCode = groupCode;
        mIsSeller = isSeller;
        mDescription = description;
        mOldId = oldId;
        mEmail = email;
        mIsStaff = isStaff;
        mIsActive = isActive;
        mDateJoined = dateJoined;
        mIsSubscribed = isSubscribed;
        mIsVerified = isVerified;
        mIsVatExempt = isVatExempt;
        mAvgRating = avgRating;
        mPhoto = photo;
        mBusinessName = businessName;
        mPostcode = postcode;
        mVatNumber = vatNumber;
        mHasNotifications = hasNotifications;
        mBusinessTypeId = businessTypeId;
        mBusinessSubtypeId = businessSubtypeId;
        mIsVerifiedByStaffMember = isVerifiedByStaffMember;
    }

    protected User(Parcel in) {
        mId = in.readInt();
        mUserName = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mBusinessTypeName = in.readString();
        mBusinessSubtypeName = in.readString();
        mLastLogin = in.readString();
        mIsSuperuser = in.readByte() != 0;
        mRole = in.readInt();
        mPartnerParent = in.readString();
        mPartner = in.readString();
        mRememberToken = in.readString();
        mPaymentMethod = in.readString();
        mAccountNumber = in.readString();
        mService = in.readString();
        mGroupReferences = in.readString();
        mGroupCode = in.readString();
        mIsSeller = in.readByte() != 0;
        mDescription = in.readString();
        mOldId = in.readString();
        mEmail = in.readString();
        mIsStaff = in.readByte() != 0;
        mIsActive = in.readByte() != 0;
        mDateJoined = in.readString();
        mIsSubscribed = in.readByte() != 0;
        mIsVerified = in.readByte() != 0;
        mIsVatExempt = in.readByte() != 0;
        mAvgRating = in.readDouble();
        mPhoto = in.readString();
        mBusinessName = in.readString();
        mPostcode = in.readString();
        mVatNumber = in.readString();
        mHasNotifications = in.readByte() != 0;
        mBusinessTypeId = in.readInt();
        mBusinessSubtypeId = in.readInt();
        mIsVerifiedByStaffMember = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mUserName);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mBusinessTypeName);
        dest.writeString(mBusinessSubtypeName);
        dest.writeString(mLastLogin);
        dest.writeByte((byte) (mIsSuperuser ? 1 : 0));
        dest.writeInt(mRole);
        dest.writeString(mPartnerParent);
        dest.writeString(mPartner);
        dest.writeString(mRememberToken);
        dest.writeString(mPaymentMethod);
        dest.writeString(mAccountNumber);
        dest.writeString(mService);
        dest.writeString(mGroupReferences);
        dest.writeString(mGroupCode);
        dest.writeByte((byte) (mIsSeller ? 1 : 0));
        dest.writeString(mDescription);
        dest.writeString(mOldId);
        dest.writeString(mEmail);
        dest.writeByte((byte) (mIsStaff ? 1 : 0));
        dest.writeByte((byte) (mIsActive ? 1 : 0));
        dest.writeString(mDateJoined);
        dest.writeByte((byte) (mIsSubscribed ? 1 : 0));
        dest.writeByte((byte) (mIsVerified ? 1 : 0));
        dest.writeByte((byte) (mIsVatExempt ? 1 : 0));
        dest.writeDouble(mAvgRating);
        dest.writeString(mPhoto);
        dest.writeString(mBusinessName);
        dest.writeString(mPostcode);
        dest.writeString(mVatNumber);
        dest.writeByte((byte) (mHasNotifications ? 1 : 0));
        dest.writeInt(mBusinessTypeId);
        dest.writeInt(mBusinessSubtypeId);
        dest.writeByte((byte) (mIsVerifiedByStaffMember ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getUserName() {
        return mUserName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getBusinessTypeName() {
        return mBusinessTypeName;
    }

    public String getBusinessSubtypeName() {
        return mBusinessSubtypeName;
    }

    public String getLastLogin() {
        return mLastLogin;
    }

    public boolean isSuperuser() {
        return mIsSuperuser;
    }

    public int getRole() {
        return mRole;
    }

    public String getPartnerParent() {
        return mPartnerParent;
    }

    public String getPartner() {
        return mPartner;
    }

    public String getRememberToken() {
        return mRememberToken;
    }

    public String getPaymentMethod() {
        return mPaymentMethod;
    }

    public String getAccountNumber() {
        return mAccountNumber;
    }

    public String getService() {
        return mService;
    }

    public String getGroupReferences() {
        return mGroupReferences;
    }

    public String getGroupCode() {
        return mGroupCode;
    }

    public boolean isSeller() {
        return mIsSeller;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getOldId() {
        return mOldId;
    }

    public String getEmail() {
        return mEmail;
    }

    public boolean isStaff() {
        return mIsStaff;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public String getDateJoined() {
        return mDateJoined;
    }

    public boolean isSubscribed() {
        return mIsSubscribed;
    }

    public boolean isVerified() {
        return mIsVerified;
    }

    public boolean isVatExempt() {
        return mIsVatExempt;
    }

    public double getAvgRating() {
        return mAvgRating;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public String getBusinessName() {
        return mBusinessName;
    }

    public String getPostcode() {
        return mPostcode;
    }

    public String getVatNumber() {
        return mVatNumber;
    }

    public boolean hasNotifications() {
        return mHasNotifications;
    }

    public int getBusinessTypeId() {
        return mBusinessTypeId;
    }

    public int getBusinessSubtypeId() {
        return mBusinessSubtypeId;
    }

    public boolean isVerifiedByStaffMember() {
        return mIsVerifiedByStaffMember;
    }

    @Override public String toString() {
        return "User{" +
                "mId=" + mId +
                ", mUserName='" + mUserName + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mBusinessTypeName='" + mBusinessTypeName + '\'' +
                ", mBusinessSubtypeName='" + mBusinessSubtypeName + '\'' +
                ", mLastLogin='" + mLastLogin + '\'' +
                ", mIsSuperuser=" + mIsSuperuser +
                ", mRole=" + mRole +
                ", mPartnerParent='" + mPartnerParent + '\'' +
                ", mPartner='" + mPartner + '\'' +
                ", mRememberToken='" + mRememberToken + '\'' +
                ", mPaymentMethod='" + mPaymentMethod + '\'' +
                ", mAccountNumber='" + mAccountNumber + '\'' +
                ", mService='" + mService + '\'' +
                ", mGroupReferences='" + mGroupReferences + '\'' +
                ", mGroupCode='" + mGroupCode + '\'' +
                ", mIsSeller=" + mIsSeller +
                ", mDescription='" + mDescription + '\'' +
                ", mOldId='" + mOldId + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mIsStaff=" + mIsStaff +
                ", mIsActive=" + mIsActive +
                ", mDateJoined='" + mDateJoined + '\'' +
                ", mIsSubscribed=" + mIsSubscribed +
                ", mIsVerified=" + mIsVerified +
                ", mIsVatExempt=" + mIsVatExempt +
                ", mAvgRating=" + mAvgRating +
                ", mPhoto='" + mPhoto + '\'' +
                ", mBusinessName='" + mBusinessName + '\'' +
                ", mPostcode='" + mPostcode + '\'' +
                ", mVatNumber='" + mVatNumber + '\'' +
                ", mHasNotifications=" + mHasNotifications +
                ", mBusinessTypeId=" + mBusinessTypeId +
                ", mBusinessSubtypeId=" + mBusinessSubtypeId +
                ", mIsVerifiedByStaffMember=" + mIsVerifiedByStaffMember +
                '}';
    }

    public static class Builder {

        private int mId;
        private String mUserName;
        private String mFirstName;
        private String mLastName;
        private String mBusinessTypeName;
        private String mBusinessSubtypeName;
        private String mLastLogin;
        private boolean mIsSuperuser;
        private int mRole;
        private String mPartnerParent;
        private String mPartner;
        private String mRememberToken;
        private String mPaymentMethod;
        private String mAccountNumber;
        private String mService;
        private String mGroupReferences;
        private String mGroupCode;
        private boolean mIsSeller;
        private String mDescription;
        private String mOldId;
        private String mEmail;
        private boolean mIsStaff;
        private boolean mIsActive;
        private String mDateJoined;
        private boolean mIsSubscribed;
        private boolean mIsVerified;
        private boolean mIsVatExempt;
        private double mAvgRating;
        private String mPhoto;
        private String mBusinessName;
        private String mPostcode;
        private String mVatNumber;
        private boolean mHasNotifications;
        private int mBusinessTypeId;
        private int mBusinessSubtypeId;
        private boolean mIsVerifiedByStaffMember;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setUserName(String userName) {
            mUserName = userName;
            return this;
        }

        public Builder setFirstName(String firstName) {
            mFirstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            mLastName = lastName;
            return this;
        }

        public Builder setBusinessTypeName(String businessTypeName) {
            mBusinessTypeName = businessTypeName;
            return this;
        }

        public Builder setBusinessSubtypeName(String businessSubtypeName) {
            mBusinessSubtypeName = businessSubtypeName;
            return this;
        }

        public Builder setLastLogin(String lastLogin) {
            mLastLogin = lastLogin;
            return this;
        }

        public Builder setIsSuperuser(boolean isSuperuser) {
            mIsSuperuser = isSuperuser;
            return this;
        }

        public Builder setRole(int role) {
            mRole = role;
            return this;
        }

        public Builder setPartnerParent(String partnerParent) {
            mPartnerParent = partnerParent;
            return this;
        }

        public Builder setPartner(String partner) {
            mPartner = partner;
            return this;
        }

        public Builder setRememberToken(String rememberToken) {
            mRememberToken = rememberToken;
            return this;
        }

        public Builder setPaymentMethod(String paymentMethod) {
            mPaymentMethod = paymentMethod;
            return this;
        }

        public Builder setAccountNumber(String accountNumber) {
            mAccountNumber = accountNumber;
            return this;
        }

        public Builder setService(String service) {
            mService = service;
            return this;
        }

        public Builder setGroupReferences(String groupReferences) {
            mGroupReferences = groupReferences;
            return this;
        }

        public Builder setGroupCode(String groupCode) {
            mGroupCode = groupCode;
            return this;
        }

        public Builder setIsSeller(boolean isSeller) {
            mIsSeller = isSeller;
            return this;
        }

        public Builder setDescription(String description) {
            mDescription = description;
            return this;
        }

        public Builder setOldId(String oldId) {
            mOldId = oldId;
            return this;
        }

        public Builder setEmail(String email) {
            mEmail = email;
            return this;
        }

        public Builder setIsStaff(boolean isStaff) {
            mIsStaff = isStaff;
            return this;
        }

        public Builder setIsActive(boolean isActive) {
            mIsActive = isActive;
            return this;
        }

        public Builder setDateJoined(String dateJoined) {
            mDateJoined = dateJoined;
            return this;
        }

        public Builder setIsSubscribed(boolean isSubscribed) {
            mIsSubscribed = isSubscribed;
            return this;
        }

        public Builder setIsVerified(boolean isVerified) {
            mIsVerified = isVerified;
            return this;
        }

        public Builder setIsVatExempt(boolean isVatExempt) {
            mIsVatExempt = isVatExempt;
            return this;
        }

        public Builder setAvgRating(double avgRating) {
            mAvgRating = avgRating;
            return this;
        }

        public Builder setPhoto(String photo) {
            mPhoto = photo;
            return this;
        }

        public Builder setBusinessName(String businessName) {
            mBusinessName = businessName;
            return this;
        }

        public Builder setPostcode(String postcode) {
            mPostcode = postcode;
            return this;
        }

        public Builder setVatNumber(String vatNumber) {
            mVatNumber = vatNumber;
            return this;
        }

        public Builder setHasNotifications(boolean hasNotifications) {
            mHasNotifications = hasNotifications;
            return this;
        }

        public Builder setBusinessTypeId(int businessTypeId) {
            mBusinessTypeId = businessTypeId;
            return this;
        }

        public Builder setBusinessSubtypeId(int businessSubtypeId) {
            mBusinessSubtypeId = businessSubtypeId;
            return this;
        }

        public Builder setVerifiedByStaffMember(boolean verifiedByStaffMember) {
            mIsVerifiedByStaffMember = verifiedByStaffMember;
            return this;
        }

        public User build() {
            return new User(mId, mUserName, mFirstName, mLastName, mBusinessTypeName, mBusinessSubtypeName,
                    mLastLogin, mIsSuperuser, mRole, mPartnerParent, mPartner, mRememberToken, mPaymentMethod,
                    mAccountNumber, mService, mGroupReferences, mGroupCode, mIsSeller, mDescription, mOldId,
                    mEmail, mIsStaff, mIsActive, mDateJoined, mIsSubscribed, mIsVerified, mIsVatExempt, mAvgRating,
                    mPhoto, mBusinessName, mPostcode, mVatNumber, mHasNotifications, mBusinessTypeId, mBusinessSubtypeId,
                    mIsVerifiedByStaffMember);
        }
    }
}
