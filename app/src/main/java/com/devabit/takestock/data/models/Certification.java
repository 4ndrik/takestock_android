package com.devabit.takestock.data.models;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class Certification {

    private int mId;
    private String mName;
    private String mDescription;
    private String mLogoUrl;

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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        mLogoUrl = logoUrl;
    }

    @Override public String toString() {
        return "Certification{" +
                "mId=" + mId +
                ", mDescription='" + mDescription + '\'' +
                ", mLogoUrl='" + mLogoUrl + '\'' +
                '}';
    }
}
