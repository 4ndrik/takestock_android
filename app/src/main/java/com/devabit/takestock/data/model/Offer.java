package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

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
    private int mStatusForBuyer;
    private String mComment;
    private String mStatusComment;
    private String mCreatedAt;
    private String mUpdatedAt;
    private Author mAuthor;
    private int mPriceForStripe;
    private int mNotifications;
    private boolean mFromSeller;
    private Object[] mShipping;
    private Offer[] mChildOffers;
    private int mLastOffer;

    public Offer() {
    }

    private Offer(int id,
                  int advertId,
                  int counterOfferId,
                  String price,
                  int quantity,
                  int userId,
                  int status,
                  int statusForBuyer,
                  String comment,
                  String statusComment,
                  String createdAt,
                  String updatedAt,
                  Author author,
                  int priceForStripe,
                  int notifications,
                  boolean fromSeller,
                  Object[] shipping,
                  Offer[] childOffers,
                  int lastOffer) {
        mId = id;
        mAdvertId = advertId;
        mCounterOfferId = counterOfferId;
        mPrice = price;
        mQuantity = quantity;
        mUserId = userId;
        mStatus = status;
        mStatusForBuyer = statusForBuyer;
        mComment = comment;
        mStatusComment = statusComment;
        mCreatedAt = createdAt;
        mUpdatedAt = updatedAt;
        mAuthor = author;
        mPriceForStripe = priceForStripe;
        mNotifications = notifications;
        mFromSeller = fromSeller;
        mShipping = shipping;
        mChildOffers = childOffers;
        mLastOffer = lastOffer;
    }

    protected Offer(Parcel in) {
        mId = in.readInt();
        mAdvertId = in.readInt();
        mCounterOfferId = in.readInt();
        mPrice = in.readString();
        mQuantity = in.readInt();
        mUserId = in.readInt();
        mStatus = in.readInt();
        mStatusForBuyer = in.readInt();
        mComment = in.readString();
        mStatusComment = in.readString();
        mCreatedAt = in.readString();
        mUpdatedAt = in.readString();
        mAuthor = in.readParcelable(Author.class.getClassLoader());
        mPriceForStripe = in.readInt();
        mNotifications = in.readInt();
        mFromSeller = in.readByte() != 0;
//        mShipping = in.createIntArray();
        mChildOffers = in.createTypedArray(Offer.CREATOR);
        mLastOffer = in.readInt();
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
        dest.writeInt(mStatusForBuyer);
        dest.writeString(mComment);
        dest.writeString(mStatusComment);
        dest.writeString(mCreatedAt);
        dest.writeString(mUpdatedAt);
        dest.writeParcelable(mAuthor, flags);
        dest.writeInt(mPriceForStripe);
        dest.writeInt(mNotifications);
        dest.writeByte((byte) (mFromSeller ? 1 : 0));
//        dest.writeIntArray(mShipping);
        dest.writeTypedArray(mChildOffers, flags);
        dest.writeInt(mLastOffer);
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

    public void setStatus(int status) {
        mStatus = status;
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

    public int getStatusForBuyer() {
        return mStatusForBuyer;
    }

    public int getPriceForStripe() {
        return mPriceForStripe;
    }

    public int getNotifications() {
        return mNotifications;
    }

    public boolean isFromSeller() {
        return mFromSeller;
    }

    public Object[] getShipping() {
        return mShipping;
    }

    public Offer[] getChildOffers() {
        return mChildOffers;
    }

    public boolean hasChildOffers() {
        return mChildOffers != null && mChildOffers.length > 0;
    }

    public int getLastOffer() {
        return mLastOffer;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Offer offer = (Offer) o;

        return mId == offer.mId;

    }

    @Override public int hashCode() {
        return mId;
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
                ", mStatusForBuyer=" + mStatusForBuyer +
                ", mComment='" + mComment + '\'' +
                ", mStatusComment='" + mStatusComment + '\'' +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                ", mUpdatedAt='" + mUpdatedAt + '\'' +
                ", mAuthor=" + mAuthor +
                ", mPriceForStripe=" + mPriceForStripe +
                ", mNotifications=" + mNotifications +
                ", mFromSeller=" + mFromSeller +
                ", mShipping=" + Arrays.toString(mShipping) +
                ", mChildOffers=" + Arrays.toString(mChildOffers) +
                ", mLastOffer=" + mLastOffer +
                '}';
    }

    public static final class Builder {

        private int mId;
        private int mAdvertId;
        private int mCounterOfferId;
        private String mPrice;
        private int mQuantity;
        private int mUserId;
        private int mStatus;
        private int mStatusForBuyer;
        private String mComment;
        private String mStatusComment;
        private String mCreatedAt;
        private String mUpdatedAt;
        private Author mAuthor;
        private int mPriceForStripe;
        private int mNotifications;
        private boolean mFromSeller;
        private Object[] mShipping;
        private Offer[] mChildOffers;
        private int mLastOffer;

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

        public Builder setStatusForBuyer(int statusForBuyer) {
            mStatusForBuyer = statusForBuyer;
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

        public Builder setPriceForStripe(int priceForStripe) {
            mPriceForStripe = priceForStripe;
            return this;
        }

        public Builder setNotifications(int notifications) {
            mNotifications = notifications;
            return this;
        }

        public Builder setFromSeller(boolean fromSeller) {
            mFromSeller = fromSeller;
            return this;
        }

        public Builder setShipping(Object[] shipping) {
            mShipping = shipping;
            return this;
        }

        public Builder setChildOffers(Offer[] childOffers) {
            mChildOffers = childOffers;
            return this;
        }

        public Builder setLastOffer(int lastOffer) {
            mLastOffer = lastOffer;
            return this;
        }

        public Offer create() {
            return new Offer(mId, mAdvertId, mCounterOfferId, mPrice, mQuantity, mUserId,
                    mStatus, mStatusForBuyer, mComment, mStatusComment, mCreatedAt, mUpdatedAt, mAuthor,
                    mPriceForStripe, mNotifications, mFromSeller, mShipping, mChildOffers, mLastOffer);
        }
    }

    public static class Accept {

        private int mId;
        private int mUserId;
        private int mOfferId;
        private int mStatus;
        private String mPrice;
        private String mComment;
        private int mQuantity;
        private String mStreet;
        private String mHouse;
        private String mCity;
        private String mPostcode;
        private String mPhone;
        private boolean mStockInTransit;
        private boolean mFromSeller;
        private String mArrivalDate;
        private String mPickUpDate;
        private String mTracking;
        private String mCourierName;

        private Accept() {
        }

        private Accept(int id,
                       int userId,
                       int offerId,
                       int status,
                       String price,
                       String comment,
                       int quantity,
                       String street,
                       String house,
                       String city,
                       String postcode,
                       String phone,
                       boolean stockInTransit,
                       boolean fromSeller,
                       String arrivalDate,
                       String pickUpDate,
                       String tracking,
                       String courierName) {
            mId = id;
            mUserId = userId;
            mOfferId = offerId;
            mStatus = status;
            mPrice = price;
            mComment = comment;
            mQuantity = quantity;
            mStreet = street;
            mHouse = house;
            mCity = city;
            mPostcode = postcode;
            mPhone = phone;
            mStockInTransit = stockInTransit;
            mFromSeller = fromSeller;
            mArrivalDate = arrivalDate;
            mPickUpDate = pickUpDate;
            mTracking = tracking;
            mCourierName = courierName;
        }

        public int getId() {
            return mId;
        }

        public int getUserId() {
            return mUserId;
        }

        public int getOfferId() {
            return mOfferId;
        }

        public int getStatus() {
            return mStatus;
        }

        public String getPrice() {
            return mPrice;
        }

        public String getComment() {
            return mComment;
        }

        public int getQuantity() {
            return mQuantity;
        }

        public String getStreet() {
            return mStreet;
        }

        public String getHouse() {
            return mHouse;
        }

        public String getCity() {
            return mCity;
        }

        public String getPostcode() {
            return mPostcode;
        }

        public String getPhone() {
            return mPhone;
        }

        public boolean isStockInTransit() {
            return mStockInTransit;
        }

        public boolean isFromSeller() {
            return mFromSeller;
        }

        public String getArrivalDate() {
            return mArrivalDate;
        }

        public String getPickUpDate() {
            return mPickUpDate;
        }

        public String getTracking() {
            return mTracking;
        }

        public String getCourierName() {
            return mCourierName;
        }

        @Override public String toString() {
            return "OfferAccept{" +
                    "mOfferId=" + mOfferId +
                    ", mStatus=" + mStatus +
                    ", mPrice='" + mPrice + '\'' +
                    ", mComment='" + mComment + '\'' +
                    ", mQuantity=" + mQuantity +
                    ", mStreet='" + mStreet + '\'' +
                    ", mHouse='" + mHouse + '\'' +
                    ", mCity='" + mCity + '\'' +
                    ", mPostcode='" + mPostcode + '\'' +
                    ", mPhone='" + mPhone + '\'' +
                    ", mStockInTransit=" + mStockInTransit +
                    ", mFromSeller=" + mFromSeller +
                    ", mArrivalDate='" + mArrivalDate + '\'' +
                    ", mPickUpDate='" + mPickUpDate + '\'' +
                    ", mTracking='" + mTracking + '\'' +
                    ", mCourierName='" + mCourierName + '\'' +
                    '}';
        }

        public static final class Builder {

            private int mId;
            private int mUserId;
            private int mOfferId;
            private int mStatus;
            private String mPrice;
            private String mComment;
            private int mQuantity;
            private String mStreet;
            private String mHouse;
            private String mCity;
            private String mPostcode;
            private String mPhone;
            private boolean mStockInTransit;
            private boolean mFromSeller;
            private String mArrivalDate;
            private String mPickUpDate;
            private String mTracking;
            private String mCourierName;

            public Builder setId(int id) {
                mId = id;
                return this;
            }

            public Builder setUserId(int userId) {
                mUserId = userId;
                return this;
            }

            public Builder setOfferId(int offerId) {
                mOfferId = offerId;
                return this;
            }

            public Builder setStatus(int status) {
                mStatus = status;
                return this;
            }

            public Builder setPrice(String price) {
                mPrice = price;
                return this;
            }

            public Builder setComment(String comment) {
                mComment = comment;
                return this;
            }

            public Builder setQuantity(int quantity) {
                mQuantity = quantity;
                return this;
            }

            public Builder setStreet(String street) {
                mStreet = street;
                return this;
            }

            public Builder setHouse(String house) {
                mHouse = house;
                return this;
            }

            public Builder setCity(String city) {
                mCity = city;
                return this;
            }

            public Builder setPostcode(String postcode) {
                mPostcode = postcode;
                return this;
            }

            public Builder setPhone(String phone) {
                mPhone = phone;
                return this;
            }

            public Builder setStockInTransit(boolean stockInTransit) {
                mStockInTransit = stockInTransit;
                return this;
            }

            public Builder setFromSeller(boolean fromSeller) {
                mFromSeller = fromSeller;
                return this;
            }

            public Builder setArrivalDate(String arrivalDate) {
                mArrivalDate = arrivalDate;
                return this;
            }

            public Builder setPickUpDate(String pickUpDate) {
                mPickUpDate = pickUpDate;
                return this;
            }

            public Builder setTracking(String tracking) {
                mTracking = tracking;
                return this;
            }

            public Builder setCourierName(String courierName) {
                mCourierName = courierName;
                return this;
            }

            public Accept create() {
                return new Accept(mId, mUserId, mOfferId, mStatus, mPrice, mComment, mQuantity, mStreet, mHouse, mCity,
                        mPostcode, mPhone, mStockInTransit, mFromSeller, mArrivalDate, mPickUpDate, mTracking, mCourierName);
            }
        }
    }
}
