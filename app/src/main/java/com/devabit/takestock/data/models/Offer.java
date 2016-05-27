package com.devabit.takestock.data.models;

import android.os.Parcel;
import android.os.Parcelable;

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
    private OfferStatus mOfferStatus;
    private String mComment;
    private String mCreatedDate;
    private String mUpdatedDate;
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
        mOfferStatus = in.readParcelable(OfferStatus.class.getClassLoader());
        mComment = in.readString();
        mCreatedDate = in.readString();
        mUpdatedDate = in.readString();
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

    public OfferStatus getOfferStatus() {
        return mOfferStatus;
    }

    public void setOfferStatus(OfferStatus offerStatus) {
        mOfferStatus = offerStatus;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public String getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        mCreatedDate = createdDate;
    }

    public String getUpdatedDate() {
        return mUpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        mUpdatedDate = updatedDate;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
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
                ", mOfferStatus=" + mOfferStatus +
                ", mComment='" + mComment + '\'' +
                ", mCreatedDate='" + mCreatedDate + '\'' +
                ", mUpdatedDate='" + mUpdatedDate + '\'' +
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
        dest.writeParcelable(mOfferStatus, flags);
        dest.writeString(mComment);
        dest.writeString(mCreatedDate);
        dest.writeString(mUpdatedDate);
        dest.writeParcelable(mUser, flags);
    }
}
