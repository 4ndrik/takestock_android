package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class Advert implements Parcelable {

    private int mId;
    private String mName;
    private String mCreatedAt;
    private String mExpiresAt;
    private String mUpdatedAt;
    private String mGuidePrice;
    private String mDescription;
    private String mLocation;
    private int mShippingId;
    private String mShippingDisplay;
    private boolean mIsVatExempt;
    private List<Photo> mPhotos;
    private int mAuthorId;
    private int mCategoryId;
    private int mSubcategoryId;
    private int mPackagingId;
    private int mMinOrderQuantity;
    private String mSize;
    private Certification mCertification;
    private String mCertificationExtra;
    private int mCertificationId;
    private int mConditionId;
    private String mConditionDisplay;
    private int mItemsCount;
    private int mItemsCountNow;
    private List<String> mTags;
    private User mUser;
    private String mPackagingName;
    private String mOffersCount;
    private String mQuestionsCount;
    private String mDaysLeft;
    private int[] mSubscribers;
    private boolean mInDrafts;
    private int mAdvertsViews;
    private boolean mCanOffer;
    private int mNotifications;
    private int mNewQuestionsCount;
    private int mNewOffersCount;
    private boolean mIsFood;
    private int mState;
    private String mEscapedDescription;
    private String mCategoryName;

    private Advert(int id, String name, String createdAt, String expiresAt, String updatedAt, String guidePrice,
                   String description, String location, int shippingId, String shippingDisplay, boolean isVatExempt,
                   List<Photo> photos, int authorId, int categoryId, int subcategoryId, int packagingId,
                   int minOrderQuantity, String size, Certification certification, String certificationExtra,
                   int certificationId, int conditionId, String conditionDisplay, int itemsCount, int itemsCountNow,
                   List<String> tags, User user, String packagingName, String offersCount, String questionsCount,
                   String daysLeft, int[] subscribers, boolean isDrafts, int advertsViews, boolean canOffer,
                   int notifications, int newQuestionsCount, int newOffersCount, boolean isFood, int state,
                   String escapedDescription, String categoryName) {
        mId = id;
        mName = name;
        mCreatedAt = createdAt;
        mExpiresAt = expiresAt;
        mUpdatedAt = updatedAt;
        mGuidePrice = guidePrice;
        mDescription = description;
        mLocation = location;
        mShippingId = shippingId;
        mShippingDisplay = shippingDisplay;
        mIsVatExempt = isVatExempt;
        mPhotos = photos;
        mAuthorId = authorId;
        mCategoryId = categoryId;
        mSubcategoryId = subcategoryId;
        mPackagingId = packagingId;
        mMinOrderQuantity = minOrderQuantity;
        mSize = size;
        mCertification = certification;
        mCertificationExtra = certificationExtra;
        mCertificationId = certificationId;
        mConditionId = conditionId;
        mConditionDisplay = conditionDisplay;
        mItemsCount = itemsCount;
        mItemsCountNow = itemsCountNow;
        mTags = tags;
        mUser = user;
        mPackagingName = packagingName;
        mOffersCount = offersCount;
        mQuestionsCount = questionsCount;
        mDaysLeft = daysLeft;
        mSubscribers = subscribers;
        mInDrafts = isDrafts;
        mAdvertsViews = advertsViews;
        mCanOffer = canOffer;
        mNotifications = notifications;
        mNewQuestionsCount = newQuestionsCount;
        mNewOffersCount = newOffersCount;
        mIsFood = isFood;
        mState = state;
        mEscapedDescription = escapedDescription;
        mCategoryName = categoryName;
    }

    protected Advert(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mCreatedAt = in.readString();
        mExpiresAt = in.readString();
        mUpdatedAt = in.readString();
        mGuidePrice = in.readString();
        mDescription = in.readString();
        mLocation = in.readString();
        mShippingId = in.readInt();
        mShippingDisplay = in.readString();
        mIsVatExempt = in.readByte() != 0;
        mPhotos = in.createTypedArrayList(Photo.CREATOR);
        mAuthorId = in.readInt();
        mCategoryId = in.readInt();
        mSubcategoryId = in.readInt();
        mPackagingId = in.readInt();
        mMinOrderQuantity = in.readInt();
        mSize = in.readString();
        mCertification = in.readParcelable(Certification.class.getClassLoader());
        mCertificationExtra = in.readString();
        mCertificationId = in.readInt();
        mConditionId = in.readInt();
        mConditionDisplay = in.readString();
        mItemsCount = in.readInt();
        mItemsCountNow = in.readInt();
        mTags = in.createStringArrayList();
        mPackagingName = in.readString();
        mOffersCount = in.readString();
        mQuestionsCount = in.readString();
        mDaysLeft = in.readString();
        mSubscribers = in.createIntArray();
        mInDrafts = in.readByte() != 0;
        mAdvertsViews = in.readInt();
        mCanOffer = in.readByte() != 0;
        mNotifications = in.readInt();
        mNewQuestionsCount = in.readInt();
        mNewOffersCount = in.readInt();
        mIsFood = in.readByte() != 0;
        mState = in.readInt();
        mEscapedDescription = in.readString();
        mCategoryName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mCreatedAt);
        dest.writeString(mExpiresAt);
        dest.writeString(mUpdatedAt);
        dest.writeString(mGuidePrice);
        dest.writeString(mDescription);
        dest.writeString(mLocation);
        dest.writeInt(mShippingId);
        dest.writeString(mShippingDisplay);
        dest.writeByte((byte) (mIsVatExempt ? 1 : 0));
        dest.writeTypedList(mPhotos);
        dest.writeInt(mAuthorId);
        dest.writeInt(mCategoryId);
        dest.writeInt(mSubcategoryId);
        dest.writeInt(mPackagingId);
        dest.writeInt(mMinOrderQuantity);
        dest.writeString(mSize);
        dest.writeParcelable(mCertification, flags);
        dest.writeString(mCertificationExtra);
        dest.writeInt(mCertificationId);
        dest.writeInt(mConditionId);
        dest.writeString(mConditionDisplay);
        dest.writeInt(mItemsCount);
        dest.writeInt(mItemsCountNow);
        dest.writeStringList(mTags);
        dest.writeString(mPackagingName);
        dest.writeString(mOffersCount);
        dest.writeString(mQuestionsCount);
        dest.writeString(mDaysLeft);
        dest.writeIntArray(mSubscribers);
        dest.writeByte((byte) (mInDrafts ? 1 : 0));
        dest.writeInt(mAdvertsViews);
        dest.writeByte((byte) (mCanOffer ? 1 : 0));
        dest.writeInt(mNotifications);
        dest.writeInt(mNewQuestionsCount);
        dest.writeInt(mNewOffersCount);
        dest.writeByte((byte) (mIsFood ? 1 : 0));
        dest.writeInt(mState);
        dest.writeString(mEscapedDescription);
        dest.writeString(mCategoryName);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getName() {
        return mName;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public String getExpiresAt() {
        return mExpiresAt;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public String getGuidePrice() {
        return mGuidePrice;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getLocation() {
        return mLocation;
    }

    public int getShippingId() {
        return mShippingId;
    }

    public String getShippingDisplay() {
        return mShippingDisplay;
    }

    public boolean isVatExempt() {
        return mIsVatExempt;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public int getAuthorId() {
        return mAuthorId;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public int getSubcategoryId() {
        return mSubcategoryId;
    }

    public int getPackagingId() {
        return mPackagingId;
    }

    public int getMinOrderQuantity() {
        return mMinOrderQuantity;
    }

    public String getSize() {
        return mSize;
    }

    public Certification getCertification() {
        return mCertification;
    }

    public String getCertificationExtra() {
        return mCertificationExtra;
    }

    public int getCertificationId() {
        return mCertificationId;
    }

    public int getConditionId() {
        return mConditionId;
    }

    public String getConditionDisplay() {
        return mConditionDisplay;
    }

    public int getItemsCount() {
        return mItemsCount;
    }

    public int getItemsCountNow() {
        return mItemsCountNow;
    }

    public List<String> getTags() {
        return mTags;
    }

    public User getUser() {
        return mUser;
    }

    public String getPackagingName() {
        return mPackagingName;
    }

    public String getOffersCount() {
        return mOffersCount;
    }

    public String getQuestionsCount() {
        return mQuestionsCount;
    }

    public String getDaysLeft() {
        return mDaysLeft;
    }

    public int[] getSubscribers() {
        return mSubscribers;
    }

    public void setInDrafts(boolean inDrafts) {
        mInDrafts = inDrafts;
    }

    public boolean isInDrafts() {
        return mInDrafts;
    }

    public int getAdvertsViews() {
        return mAdvertsViews;
    }

    public void setCanOffer(boolean canOffer) {
        mCanOffer = canOffer;
    }

    public boolean canOffer() {
        return mCanOffer;
    }

    public int getNotifications() {
        return mNotifications;
    }

    public boolean hasNotifications() {
        return mNotifications > 0;
    }

    public int getNewQuestionsCount() {
        return mNewQuestionsCount;
    }

    public int getNewOffersCount() {
        return mNewOffersCount;
    }

    public boolean isFood() {
        return mIsFood;
    }

    public void setState(int state) {
        mState = state;
    }

    public int getState() {
        return mState;
    }

    public String getEscapedDescription() {
        return mEscapedDescription;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void removeSubscriber(int userId) {
        for (int i = 0; i < mSubscribers.length; i++) {
            if (mSubscribers[i] == userId) {
                int[] newSubscribers = (int[]) Array.newInstance(mSubscribers.getClass().getComponentType(), mSubscribers.length - 1);
                System.arraycopy(mSubscribers, 0, newSubscribers, 0, i);
                if (i < mSubscribers.length - 1) {
                    System.arraycopy(mSubscribers, i + 1, newSubscribers, i, mSubscribers.length - i - 1);
                }
                mSubscribers = newSubscribers;
                break;
            }
        }
    }

    public void addSubscriber(int userId) {
        int[] newSubscribers = copyArrayGrow1(mSubscribers);
        newSubscribers[newSubscribers.length - 1] = userId;
        mSubscribers = newSubscribers;
    }

    public boolean hasSubscriber(int userId) {
        for (int id : mSubscribers) {
            if (id == userId) return true;
        }
        return false;
    }

    private int[] copyArrayGrow1(int[] array) {
        if (array != null) {
            int arrayLength = Array.getLength(array);
            int[] newArray = (int[]) Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
            return newArray;
        }
        return (int[]) Array.newInstance(Integer.TYPE, 1);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Advert advert = (Advert) o;

        return mId == advert.mId;
    }

    @Override public int hashCode() {
        return mId;
    }

    @Override public String toString() {
        return "Advert{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                ", mExpiresAt='" + mExpiresAt + '\'' +
                ", mUpdatedAt='" + mUpdatedAt + '\'' +
                ", mGuidePrice='" + mGuidePrice + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mShippingId=" + mShippingId +
                ", mShippingDisplay='" + mShippingDisplay + '\'' +
                ", mIsVatExempt=" + mIsVatExempt +
                ", mPhotos=" + mPhotos +
                ", mAuthorId=" + mAuthorId +
                ", mCategoryId=" + mCategoryId +
                ", mSubcategoryId=" + mSubcategoryId +
                ", mPackagingId=" + mPackagingId +
                ", mMinOrderQuantity=" + mMinOrderQuantity +
                ", mSize='" + mSize + '\'' +
                ", mCertification=" + mCertification +
                ", mCertificationExtra='" + mCertificationExtra + '\'' +
                ", mCertificationId=" + mCertificationId +
                ", mConditionId=" + mConditionId +
                ", mConditionDisplay='" + mConditionDisplay + '\'' +
                ", mItemsCount=" + mItemsCount +
                ", mItemsCountNow=" + mItemsCountNow +
                ", mTags=" + mTags +
                ", mAuthor=" + mUser +
                ", mPackagingName='" + mPackagingName + '\'' +
                ", mOffersCount='" + mOffersCount + '\'' +
                ", mQuestionsCount='" + mQuestionsCount + '\'' +
                ", mDaysLeft='" + mDaysLeft + '\'' +
                ", mSubscribers=" + Arrays.toString(mSubscribers) +
                ", mInDrafts=" + mInDrafts +
                ", mAdvertsViews=" + mAdvertsViews +
                ", mCanOffer=" + mCanOffer +
                ", mNotifications=" + mNotifications +
                ", mNewQuestionsCount=" + mNewQuestionsCount +
                ", mNewOffersCount=" + mNewOffersCount +
                ", mIsFood=" + mIsFood +
                ", mState=" + mState +
                ", mEscapedDescription='" + mEscapedDescription + '\'' +
                ", mCategoryName='" + mCategoryName + '\'' +
                '}';
    }

    public static final class State {
        public static final int LIVE = 1;
        public static final int ON_HOLD = 2;
        public static final int SOLD_OUT = 3;
    }

    public static class Builder {
        private int mId;
        private String mName;
        private String mCreatedAt;
        private String mExpiresAt;
        private String mUpdatedAt;
        private String mGuidePrice;
        private String mDescription;
        private String mLocation;
        private int mShippingId;
        private String mShippingDisplay;
        private boolean mIsVatExempt;
        private List<Photo> mPhotos;
        private int mAuthorId;
        private int mCategoryId;
        private int mSubcategoryId;
        private int mPackagingId;
        private int mMinOrderQuantity;
        private String mSize;
        private Certification mCertification;
        private String mCertificationExtra;
        private int mCertificationId;
        private int mConditionId;
        private String mConditionDisplay;
        private int mItemsCount;
        private int mItemsCountNow;
        private List<String> mTags;
        private User mUser;
        private String mPackagingName;
        private String mOffersCount;
        private String mQuestionsCount;
        private String mDaysLeft;
        private int[] mSubscribers;
        private boolean mInDrafts;
        private int mAdvertsViews;
        private boolean mCanOffer;
        private int mNotifications;
        private int mNewQuestionsCount;
        private int mNewOffersCount;
        private boolean mIsFood;
        private int mState;
        private String mEscapedDescription;
        private String mCategoryName;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setCreatedAt(String createdAt) {
            mCreatedAt = createdAt;
            return this;
        }

        public Builder setExpiresAt(String expiresAt) {
            mExpiresAt = expiresAt;
            return this;
        }

        public Builder setUpdatedAt(String updatedAt) {
            mUpdatedAt = updatedAt;
            return this;
        }

        public Builder setGuidePrice(String guidePrice) {
            mGuidePrice = guidePrice;
            return this;
        }

        public Builder setDescription(String description) {
            mDescription = description;
            return this;
        }

        public Builder setLocation(String location) {
            mLocation = location;
            return this;
        }

        public Builder setShippingId(int shippingId) {
            mShippingId = shippingId;
            return this;
        }

        public Builder setShippingDisplay(String shippingDisplay) {
            mShippingDisplay = shippingDisplay;
            return this;
        }

        public Builder setIsVatExempt(boolean isVatExempt) {
            mIsVatExempt = isVatExempt;
            return this;
        }

        public Builder setPhotos(List<Photo> photos) {
            mPhotos = photos;
            return this;
        }

        public Builder setAuthorId(int authorId) {
            mAuthorId = authorId;
            return this;
        }

        public Builder setCategoryId(int categoryId) {
            mCategoryId = categoryId;
            return this;
        }

        public Builder setSubcategoryId(int subcategoryId) {
            mSubcategoryId = subcategoryId;
            return this;
        }

        public Builder setPackagingId(int packagingId) {
            mPackagingId = packagingId;
            return this;
        }

        public Builder setMinOrderQuantity(int minOrderQuantity) {
            mMinOrderQuantity = minOrderQuantity;
            return this;
        }

        public Builder setSize(String size) {
            mSize = size;
            return this;
        }

        public Builder setCertification(Certification certification) {
            mCertification = certification;
            return this;
        }

        public Builder setCertificationExtra(String certificationExtra) {
            mCertificationExtra = certificationExtra;
            return this;
        }

        public Builder setCertificationId(int certificationId) {
            mCertificationId = certificationId;
            return this;
        }

        public Builder setConditionId(int conditionId) {
            mConditionId = conditionId;
            return this;
        }

        public Builder setConditionDisplay(String conditionDisplay) {
            mConditionDisplay = conditionDisplay;
            return this;
        }

        public Builder setItemsCount(int itemsCount) {
            mItemsCount = itemsCount;
            return this;
        }

        public Builder setItemsCountNow(int itemsCountNow) {
            mItemsCountNow = itemsCountNow;
            return this;
        }

        public Builder setTags(List<String> tags) {
            mTags = tags;
            return this;
        }

        public Builder setUser(User user) {
            mUser = user;
            return this;
        }

        public Builder setPackagingName(String packagingName) {
            mPackagingName = packagingName;
            return this;
        }

        public Builder setOffersCount(String offersCount) {
            mOffersCount = offersCount;
            return this;
        }

        public Builder setQuestionsCount(String questionsCount) {
            mQuestionsCount = questionsCount;
            return this;
        }

        public Builder setDaysLeft(String daysLeft) {
            mDaysLeft = daysLeft;
            return this;
        }

        public Builder setSubscribers(int[] subscribers) {
            mSubscribers = subscribers;
            return this;
        }

        public Builder setInDrafts(boolean inDrafts) {
            mInDrafts = inDrafts;
            return this;
        }

        public Builder setAdvertsViews(int advertsViews) {
            mAdvertsViews = advertsViews;
            return this;
        }

        public Builder setCanOffer(boolean canOffer) {
            mCanOffer = canOffer;
            return this;
        }

        public Builder setNotifications(int notifications) {
            mNotifications = notifications;
            return this;
        }

        public Builder setNewQuestionsCount(int newQuestionsCount) {
            mNewQuestionsCount = newQuestionsCount;
            return this;
        }

        public Builder setNewOffersCount(int newOffersCount) {
            mNewOffersCount = newOffersCount;
            return this;
        }

        public Builder setIsFood(boolean isFood) {
            mIsFood = isFood;
            return this;
        }

        public Builder setState(int state) {
            mState = state;
            return this;
        }

        public Builder setEscapedDescription(String escapedDescription) {
            mEscapedDescription = escapedDescription;
            return this;
        }

        public Builder setCategoryName(String categoryName) {
            mCategoryName = categoryName;
            return this;
        }

        public Advert create() {
            return new Advert(mId, mName, mCreatedAt, mExpiresAt, mUpdatedAt, mGuidePrice, mDescription, mLocation,
                    mShippingId, mShippingDisplay, mIsVatExempt, mPhotos, mAuthorId, mCategoryId, mSubcategoryId,
                    mPackagingId, mMinOrderQuantity, mSize, mCertification, mCertificationExtra, mCertificationId,
                    mConditionId, mConditionDisplay, mItemsCount, mItemsCountNow, mTags, mUser, mPackagingName,
                    mOffersCount, mQuestionsCount, mDaysLeft, mSubscribers, mInDrafts, mAdvertsViews, mCanOffer,
                    mNotifications, mNewQuestionsCount, mNewOffersCount, mIsFood, mState, mEscapedDescription, mCategoryName);
        }
    }

    public static class Subscriber {

        private static final String SUBSCRIBED = "subscribed";
        private static final String UNSUBSCRIBED = "unsubscribed";

        private int mAdvertId;
        private String mStatus;

        public Subscriber(int advertId, String status) {
            mAdvertId = advertId;
            mStatus = status;
        }

        public int getAdvertId() {
            return mAdvertId;
        }

        public String getStatus() {
            return mStatus;
        }

        public boolean isSubscribed() {
            return SUBSCRIBED.equals(mStatus);
        }

        @Override public String toString() {
            return "Subscriber{" +
                    "mAdvertId=" + mAdvertId +
                    ", mStatus='" + mStatus + '\'' +
                    '}';
        }
    }
}
