package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class Offer implements Parcelable {

    public static class Status {
        public static final int ACCEPTED = 1;
        public static final int REJECTED = 2;
        public static final int PENDING = 3;
        public static final int COUNTERED = 4;
        public static final int PAYMENT_MADE = 5;
        public static final int COUNTERED_BY_BUYER = 6;
        public static final int ADDRESS_RECEIVED = 7;
        public static final int AWAIT_CONFIRM_STOCK_DISPATCHED = 8;
        public static final int STOCK_IN_TRANSIT = 9;
        public static final int GOODS_RECEIVED = 10;
        public static final int IN_DISPUTE = 11;
    }

    private int mId;
    private int mAdvertId;
    private int mCounterOfferId;
    private String mPrice;
    private int mQuantity;
    private int mUserId;
    private int mStatus;
    private String mComment;
    private String mStatusComment;
    private String mCreatedAt;
    private String mUpdatedAt;
    private Author mAuthor;

    public Offer() {
    }

    private Offer(int id,
                  int advertId,
                  int counterOfferId,
                  String price,
                  int quantity,
                  int userId,
                  int status,
                  String comment,
                  String statusComment,
                  String createdAt,
                  String updatedAt,
                  Author author) {
        mId = id;
        mAdvertId = advertId;
        mCounterOfferId = counterOfferId;
        mPrice = price;
        mQuantity = quantity;
        mUserId = userId;
        mStatus = status;
        mComment = comment;
        mStatusComment = statusComment;
        mCreatedAt = createdAt;
        mUpdatedAt = updatedAt;
        mAuthor = author;
    }

    protected Offer(Parcel in) {
        mId = in.readInt();
        mAdvertId = in.readInt();
        mCounterOfferId = in.readInt();
        mPrice = in.readString();
        mQuantity = in.readInt();
        mUserId = in.readInt();
        mStatus = in.readInt();
        mComment = in.readString();
        mStatusComment = in.readString();
        mCreatedAt = in.readString();
        mUpdatedAt = in.readString();
        mAuthor = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mAdvertId);
        dest.writeInt(mCounterOfferId);
        dest.writeString(mPrice);
        dest.writeInt(mQuantity);
        dest.writeInt(mUserId);
        dest.writeInt(mStatus);
        dest.writeString(mComment);
        dest.writeString(mStatusComment);
        dest.writeString(mCreatedAt);
        dest.writeString(mUpdatedAt);
        dest.writeParcelable(mAuthor, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public int getAdvertId() {
        return mAdvertId;
    }

    public String getPrice() {
        return mPrice;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public int getUserId() {
        return mUserId;
    }

    public int getStatus() {
        return mStatus;
    }

    public String getComment() {
        return mComment;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public int getCounterOfferId() {
        return mCounterOfferId;
    }

    public String getStatusComment() {
        return mStatusComment;
    }

    public Author getAuthor() {
        return mAuthor;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Offer offer = (Offer) o;

        if (mId != offer.mId) return false;
        if (mAdvertId != offer.mAdvertId) return false;
        if (mCounterOfferId != offer.mCounterOfferId) return false;
        if (mQuantity != offer.mQuantity) return false;
        if (mUserId != offer.mUserId) return false;
        if (mStatus != offer.mStatus) return false;
        if (mPrice != null ? !mPrice.equals(offer.mPrice) : offer.mPrice != null) return false;
        if (mComment != null ? !mComment.equals(offer.mComment) : offer.mComment != null) return false;
        if (mStatusComment != null ? !mStatusComment.equals(offer.mStatusComment) : offer.mStatusComment != null)
            return false;
        if (mCreatedAt != null ? !mCreatedAt.equals(offer.mCreatedAt) : offer.mCreatedAt != null) return false;
        if (mUpdatedAt != null ? !mUpdatedAt.equals(offer.mUpdatedAt) : offer.mUpdatedAt != null) return false;
        return mAuthor != null ? mAuthor.equals(offer.mAuthor) : offer.mAuthor == null;

    }

    @Override public int hashCode() {
        int result = mId;
        result = 31 * result + mAdvertId;
        result = 31 * result + mCounterOfferId;
        result = 31 * result + (mPrice != null ? mPrice.hashCode() : 0);
        result = 31 * result + mQuantity;
        result = 31 * result + mUserId;
        result = 31 * result + mStatus;
        result = 31 * result + (mComment != null ? mComment.hashCode() : 0);
        result = 31 * result + (mStatusComment != null ? mStatusComment.hashCode() : 0);
        result = 31 * result + (mCreatedAt != null ? mCreatedAt.hashCode() : 0);
        result = 31 * result + (mUpdatedAt != null ? mUpdatedAt.hashCode() : 0);
        result = 31 * result + (mAuthor != null ? mAuthor.hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "Offer{" +
                "mId=" + mId +
                ", mAdvertId=" + mAdvertId +
                ", mCounterOfferId=" + mCounterOfferId +
                ", mPrice='" + mPrice + '\'' +
                ", mQuantity=" + mQuantity +
                ", mUserId=" + mUserId +
                ", mStatus=" + mStatus +
                ", mComment='" + mComment + '\'' +
                ", mStatusComment='" + mStatusComment + '\'' +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                ", mUpdatedAt='" + mUpdatedAt + '\'' +
                ", mAuthor=" + mAuthor +
                '}';
    }

    public static class Builder {

        private int mId;
        private int mAdvertId;
        private int mCounterOfferId;
        private String mPrice;
        private int mQuantity;
        private int mUserId;
        private int mStatus;
        private String mComment;
        private String mStatusComment;
        private String mCreatedAt;
        private String mUpdatedAt;
        private Author mAuthor;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setAdvertId(int advertId) {
            mAdvertId = advertId;
            return this;
        }

        public Builder setCounterOfferId(int counterOfferId) {
            mCounterOfferId = counterOfferId;
            return this;
        }

        public Builder setPrice(String price) {
            mPrice = price;
            return this;
        }

        public Builder setQuantity(int quantity) {
            mQuantity = quantity;
            return this;
        }

        public Builder setUserId(int userId) {
            mUserId = userId;
            return this;
        }

        public Builder setStatus(int status) {
            mStatus = status;
            return this;
        }

        public Builder setComment(String comment) {
            mComment = comment;
            return this;
        }

        public Builder setStatusComment(String statusComment) {
            mStatusComment = statusComment;
            return this;
        }

        public Builder setCreatedAt(String createdAt) {
            mCreatedAt = createdAt;
            return this;
        }

        public Builder setUpdatedAt(String updatedAt) {
            mUpdatedAt = updatedAt;
            return this;
        }

        public Builder setAuthor(Author author) {
            mAuthor = author;
            return this;
        }

        public Offer create() {
            return new Offer(mId, mAdvertId, mCounterOfferId, mPrice, mQuantity, mUserId,
                    mStatus, mComment, mStatusComment, mCreatedAt, mUpdatedAt, mAuthor);
        }
    }
}
