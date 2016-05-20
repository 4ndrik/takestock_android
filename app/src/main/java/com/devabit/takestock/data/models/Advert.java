package com.devabit.takestock.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class Advert implements Parcelable {

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
    private User mUser;
    private List<String> mTags;
    private List<Photo> mPhotos;

    public Advert(){}

    protected Advert(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mDateCreatedAt = in.readString();
        mDateExpiresAt = in.readString();
        mDateUpdatedAt = in.readString();
        mIntendedUse = in.readString();
        mGuidePrice = in.readString();
        mDescription = in.readString();
        mLocation = in.readString();
        mIsVatExempt = in.readByte() != 0;
        mShippingId = in.readInt();
        mCategoryId = in.readInt();
        mSubCategoryId = in.readInt();
        mPackagingId = in.readInt();
        mCertificationId = in.readInt();
        mConditionId = in.readInt();
        mMinOrderQuantity = in.readInt();
        mSize = in.readString();
        mCertificationExtra = in.readString();
        mItemsCount = in.readInt();
        mAuthorId = in.readInt();
        mUser = in.readParcelable(User.class.getClassLoader());
        mTags = in.createStringArrayList();
        mPhotos = in.createTypedArrayList(Photo.CREATOR);
    }

    public static final Creator<Advert> CREATOR = new Creator<Advert>() {
        @Override
        public Advert createFromParcel(Parcel in) {
            return new Advert(in);
        }

        @Override
        public Advert[] newArray(int size) {
            return new Advert[size];
        }
    };

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

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public List<String> getTags() {
        return mTags;
    }

    public void setTags(List<String> tags) {
        mTags = tags;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    @Override public String toString() {
        return "Advert{" +
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
                ", mAuthor=" + mUser +
                ", mTags=" + mTags +
                ", mPhotos=" + mPhotos +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mDateCreatedAt);
        dest.writeString(mDateExpiresAt);
        dest.writeString(mDateUpdatedAt);
        dest.writeString(mIntendedUse);
        dest.writeString(mGuidePrice);
        dest.writeString(mDescription);
        dest.writeString(mLocation);
        dest.writeByte((byte) (mIsVatExempt ? 1 : 0));
        dest.writeInt(mShippingId);
        dest.writeInt(mCategoryId);
        dest.writeInt(mSubCategoryId);
        dest.writeInt(mPackagingId);
        dest.writeInt(mCertificationId);
        dest.writeInt(mConditionId);
        dest.writeInt(mMinOrderQuantity);
        dest.writeString(mSize);
        dest.writeString(mCertificationExtra);
        dest.writeInt(mItemsCount);
        dest.writeInt(mAuthorId);
        dest.writeParcelable(mUser, flags);
        dest.writeStringList(mTags);
        dest.writeTypedList(mPhotos);
    }
}
