package com.devabit.takestock.data.source.local.realmModel;

import com.devabit.takestock.data.model.User;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 09/10/2016.
 */

public class UserRealm extends RealmObject {

    @PrimaryKey private int mId;
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

    public UserRealm(){}

    public UserRealm(User user) {
        mId = user.getId();
        mUserName = user.getUserName();
        mFirstName = user.getFirstName();
        mLastName = user.getLastName();
        mBusinessTypeName = user.getBusinessTypeName();
        mBusinessSubtypeName = user.getBusinessSubtypeName();
        mLastLogin = user.getLastLogin();
        mIsSuperuser = user.isSuperuser();
        mRole = user.getRole();
        mPartnerParent = user.getPartnerParent();
        mPartner = user.getPartner();
        mRememberToken = user.getRememberToken();
        mPaymentMethod = user.getPaymentMethod();
        mAccountNumber = user.getAccountNumber();
        mService = user.getService();
        mGroupReferences = user.getGroupReferences();
        mGroupCode = user.getGroupCode();
        mIsSeller = user.isSeller();
        mDescription = user.getDescription();
        mOldId = user.getOldId();
        mEmail = user.getEmail();
        mIsStaff = user.isStaff();
        mIsActive = user.isActive();
        mDateJoined = user.getDateJoined();
        mIsSubscribed = user.isSubscribed();
        mIsVerified = user.isVerified();
        mIsVatExempt = user.isVatExempt();
        mAvgRating = user.getAvgRating();
        mPhoto = user.getPhoto();
        mBusinessName = user.getBusinessName();
        mPostcode = user.getPostcode();
        mVatNumber = user.getVatNumber();
        mHasNotifications = user.hasNotifications();
        mBusinessTypeId = user.getBusinessTypeId();
        mBusinessSubtypeId = user.getBusinessSubtypeId();
    }

    public User getUser() {
        return new User.Builder()
                .setId(mId)
                .setBusinessName(mBusinessName)
                .setBusinessSubtypeName(mBusinessSubtypeName)
                .setLastLogin(mLastLogin)
                .setIsSuperuser(mIsSuperuser)
                .setRole(mRole)
                .setPartnerParent(mPartnerParent)
                .setPartner(mPartner)
                .setRememberToken(mRememberToken)
                .setPaymentMethod(mPaymentMethod)
                .setAccountNumber(mAccountNumber)
                .setService(mService)
                .setGroupReferences(mGroupReferences)
                .setGroupCode(mGroupCode)
                .setIsSeller(mIsSeller)
                .setOldId(mOldId)
                .setUserName(mUserName)
                .setFirstName(mFirstName)
                .setLastName(mLastName)
                .setEmail(mEmail)
                .setIsStaff(mIsStaff)
                .setIsActive(mIsActive)
                .setDateJoined(mDateJoined)
                .setIsSubscribed(mIsSubscribed)
                .setIsVerified(mIsVerified)
                .setIsVatExempt(mIsVatExempt)
                .setAvgRating(mAvgRating)
                .setBusinessName(mBusinessName)
                .setPhoto(mPhoto)
                .setPostcode(mPostcode)
                .setVatNumber(mVatNumber)
                .setHasNotifications(mHasNotifications)
                .setBusinessTypeId(mBusinessTypeId)
                .setBusinessSubtypeId(mBusinessSubtypeId)
                .build();

    }
}
