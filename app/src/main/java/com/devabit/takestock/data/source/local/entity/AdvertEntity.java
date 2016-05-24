package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 23/05/2016.
 */
public class AdvertEntity extends RealmObject {

    @PrimaryKey
    private int mId;
    private String mName;
    private String mDateCreatedAt;
    private String mDateExpiresAt;
    private String mDateUpdatedAt;
    private String mIntendedUse;
    private String mGuidePrice;
    private String mDescription;
    private String mLocation;
    private boolean mIsVatExempt;
    private int mShippingId;
    private int mCategoryId;
    private int mSubCategoryId;
    private int mPackagingId;
    private int mCertificationId;
    private int mConditionId;
    private int mMinOrderQuantity;
    private String mSize;
    private String mCertificationExtra;
    private int mItemsCount;
    private int mAuthorId;
//    private User mUser;
    private RealmList<StringEntity> mTags;
    private RealmList<PhotoEntity> mPhotos;
    private String mPackagingName;
    private String mOffersCount;
    private String mQuestionsCount;
    private String mDaysLeft;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDateCreatedAt() {
        return mDateCreatedAt;
    }

    public void setDateCreatedAt(String dateCreatedAt) {
        mDateCreatedAt = dateCreatedAt;
    }

    public String getDateExpiresAt() {
        return mDateExpiresAt;
    }

    public void setDateExpiresAt(String dateExpiresAt) {
        mDateExpiresAt = dateExpiresAt;
    }

    public String getDateUpdatedAt() {
        return mDateUpdatedAt;
    }

    public void setDateUpdatedAt(String dateUpdatedAt) {
        mDateUpdatedAt = dateUpdatedAt;
    }

    public String getIntendedUse() {
        return mIntendedUse;
    }

    public void setIntendedUse(String intendedUse) {
        mIntendedUse = intendedUse;
    }

    public String getGuidePrice() {
        return mGuidePrice;
    }

    public void setGuidePrice(String guidePrice) {
        mGuidePrice = guidePrice;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public boolean isVatExempt() {
        return mIsVatExempt;
    }

    public void setVatExempt(boolean vatExempt) {
        mIsVatExempt = vatExempt;
    }

    public int getShippingId() {
        return mShippingId;
    }

    public void setShippingId(int shippingId) {
        mShippingId = shippingId;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public int getSubCategoryId() {
        return mSubCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        mSubCategoryId = subCategoryId;
    }

    public int getPackagingId() {
        return mPackagingId;
    }

    public void setPackagingId(int packagingId) {
        mPackagingId = packagingId;
    }

    public int getCertificationId() {
        return mCertificationId;
    }

    public void setCertificationId(int certificationId) {
        mCertificationId = certificationId;
    }

    public int getConditionId() {
        return mConditionId;
    }

    public void setConditionId(int conditionId) {
        mConditionId = conditionId;
    }

    public int getMinOrderQuantity() {
        return mMinOrderQuantity;
    }

    public void setMinOrderQuantity(int minOrderQuantity) {
        mMinOrderQuantity = minOrderQuantity;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public String getCertificationExtra() {
        return mCertificationExtra;
    }

    public void setCertificationExtra(String certificationExtra) {
        mCertificationExtra = certificationExtra;
    }

    public int getItemsCount() {
        return mItemsCount;
    }

    public void setItemsCount(int itemsCount) {
        mItemsCount = itemsCount;
    }

    public int getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(int authorId) {
        mAuthorId = authorId;
    }

//    public User getUser() {
//        return mUser;
//    }
//
//    public void setUser(User user) {
//        mUser = user;
//    }

    public RealmList<StringEntity> getTags() {
        return mTags;
    }

    public void addTag(StringEntity tag) {
        mTags.add(tag);
    }

    public RealmList<PhotoEntity> getPhotos() {
        return mPhotos;
    }

    public void addPhoto(PhotoEntity photo) {
        mPhotos.add(photo);
    }

    public String getPackagingName() {
        return mPackagingName;
    }

    public void setPackagingName(String packagingName) {
        mPackagingName = packagingName;
    }

    public String getOffersCount() {
        return mOffersCount;
    }

    public void setOffersCount(String offersCount) {
        mOffersCount = offersCount;
    }

    public String getQuestionsCount() {
        return mQuestionsCount;
    }

    public void setQuestionsCount(String questionsCount) {
        mQuestionsCount = questionsCount;
    }

    public String getDaysLeft() {
        return mDaysLeft;
    }

    public void setDaysLeft(String daysLeft) {
        mDaysLeft = daysLeft;
    }

    @Override public String toString() {
        return "AdvertEntity{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mDateCreatedAt='" + mDateCreatedAt + '\'' +
                ", mDateExpiresAt='" + mDateExpiresAt + '\'' +
                ", mDateUpdatedAt='" + mDateUpdatedAt + '\'' +
                ", mIntendedUse='" + mIntendedUse + '\'' +
                ", mGuidePrice='" + mGuidePrice + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mIsVatExempt=" + mIsVatExempt +
                ", mShippingId=" + mShippingId +
                ", mCategoryId=" + mCategoryId +
                ", mSubCategoryId=" + mSubCategoryId +
                ", mPackagingId=" + mPackagingId +
                ", mCertificationId=" + mCertificationId +
                ", mConditionId=" + mConditionId +
                ", mMinOrderQuantity=" + mMinOrderQuantity +
                ", mSize='" + mSize + '\'' +
                ", mCertificationExtra='" + mCertificationExtra + '\'' +
                ", mItemsCount=" + mItemsCount +
                ", mAuthorId=" + mAuthorId +
                ", mTags=" + mTags +
                ", mPhotos=" + mPhotos +
                ", mPackagingName='" + mPackagingName + '\'' +
                ", mOffersCount='" + mOffersCount + '\'' +
                ", mQuestionsCount='" + mQuestionsCount + '\'' +
                ", mDaysLeft='" + mDaysLeft + '\'' +
                '}';
    }
}
