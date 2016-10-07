package com.devabit.takestock.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Certification implements Parcelable {

    private final int mId;
    private final String mName;
    private final String mDescription;
    private final String mLogoUrl;

    private Certification(int id, String name, String description, String logoUrl) {
        mId = id;
        mName = name;
        mDescription = description;
        mLogoUrl = logoUrl;
    }

    protected Certification(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mDescription = in.readString();
        mLogoUrl = in.readString();
    }

    public static final Creator<Certification> CREATOR = new Creator<Certification>() {
        @Override
        public Certification createFromParcel(Parcel in) {
            return new Certification(in);
        }

        @Override
        public Certification[] newArray(int size) {
            return new Certification[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeString(mLogoUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    @Override public String toString() {
        return "Certification{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mLogoUrl='" + mLogoUrl + '\'' +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Certification that = (Certification) o;

        return mId == that.mId;

    }

    @Override public int hashCode() {
        return mId;
    }

    public static class Builder {

        private int mId;
        private String mName;
        private String mDescription;
        private String mLogoUrl;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setDescription(String description) {
            mDescription = description;
            return this;
        }

        public Builder setLogoUrl(String logoUrl) {
            mLogoUrl = logoUrl;
            return this;
        }

        public Certification build() {
            return new Certification(mId, mName, mDescription, mLogoUrl);
        }
    }
}
