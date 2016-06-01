package com.devabit.takestock.data.source.local.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class OfferEntity extends RealmObject {

    @PrimaryKey
    private int mId;
    private int mAdvertId;
    private int mOfferId;
    private String mPrice;
    private int mQuantity;
    private int mUserId;
    private int mOfferStatusId;
    private String mComment;
    private String mCreatedDate;
    private String mUpdatedDate;
    private UserEntity mUser;

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

    public void setOfferStatusId(int id) {
        mOfferStatusId = id;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public String getDateCreated() {
        return mCreatedDate;
    }

    public void setDateCreated(String createdDate) {
        mCreatedDate = createdDate;
    }

    public String getUpdatedDate() {
        return mUpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        mUpdatedDate = updatedDate;
    }

    public UserEntity getUser() {
        return mUser;
    }

    public void setUser(UserEntity user) {
        mUser = user;
    }

    @Override public String toString() {
        return "OfferEntity{" +
                "mId=" + mId +
                ", mAdvertId=" + mAdvertId +
                ", mOfferId=" + mOfferId +
                ", mPrice='" + mPrice + '\'' +
                ", mQuantity=" + mQuantity +
                ", mUserId=" + mUserId +
                ", mOfferStatusId=" + mOfferStatusId +
                ", mComment='" + mComment + '\'' +
                ", mCreatedDate='" + mCreatedDate + '\'' +
                ", mUpdatedDate='" + mUpdatedDate + '\'' +
                ", mUser=" + mUser +
                '}';
    }
}
