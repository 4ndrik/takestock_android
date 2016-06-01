package com.devabit.takestock.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class Offer implements Parcelable {

    private int mId;
    private int mAdvertId;
    private int mOfferId;
    private String mPrice;
    private int mQuantity;
    private int mUserId;
    private int mOfferStatusId;
    private String mComment;
    private String mDateCreated;
    private String mDateUpdated;
    private User mUser;

    public Offer(){}

    protected Offer(Parcel in) {
        mId = in.readInt();
        mAdvertId = in.readInt();
        mOfferId = in.readInt();
        mPrice = in.readString();
        mQuantity = in.readInt();
        mUserId = in.readInt();
        mOfferStatusId = in.readInt();
        mComment = in.readString();
        mDateCreated = in.readString();
        mDateUpdated = in.readString();
        mUser = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Offer> CREATOR = new Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getAdvertId() {
        return mAdvertId;
    }

    public void setAdvertId(int advertId) {
        mAdvertId = advertId;
    }

    public int getOfferId() {
        return mOfferId;
    }

    public void setOfferId(int offerId) {
        mOfferId = offerId;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getOfferStatusId() {
        return mOfferStatusId;
    }

    public void setOfferStatusId(int offerStatusId) {
        mOfferStatusId = offerStatusId;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return mDateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        mDateUpdated = dateUpdated;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return mId == offer.mId &&
                mAdvertId == offer.mAdvertId &&
                mOfferId == offer.mOfferId &&
                mQuantity == offer.mQuantity &&
                mUserId == offer.mUserId &&
                mOfferStatusId == offer.mOfferStatusId &&
                Objects.equals(mPrice, offer.mPrice) &&
                Objects.equals(mComment, offer.mComment) &&
                Objects.equals(mDateCreated, offer.mDateCreated) &&
                Objects.equals(mDateUpdated, offer.mDateUpdated);
    }

    @Override public int hashCode() {
        return Objects.hash(mId, mAdvertId, mOfferId, mPrice, mQuantity, mUserId, mOfferStatusId, mComment, mDateCreated, mDateUpdated);
    }

    @Override public String toString() {
        return "Offer{" +
                "mId=" + mId +
                ", mAdvertId=" + mAdvertId +
                ", mOfferId=" + mOfferId +
                ", mPrice='" + mPrice + '\'' +
                ", mQuantity=" + mQuantity +
                ", mUserId=" + mUserId +
                ", mOfferStatusId=" + mOfferStatusId +
                ", mComment='" + mComment + '\'' +
                ", mDateCreated='" + mDateCreated + '\'' +
                ", mDateUpdated='" + mDateUpdated + '\'' +
                ", mUser=" + mUser +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mAdvertId);
        dest.writeInt(mOfferId);
        dest.writeString(mPrice);
        dest.writeInt(mQuantity);
        dest.writeInt(mUserId);
        dest.writeInt(mOfferStatusId);
        dest.writeString(mComment);
        dest.writeString(mDateCreated);
        dest.writeString(mDateUpdated);
        dest.writeParcelable(mUser, flags);
    }
}
