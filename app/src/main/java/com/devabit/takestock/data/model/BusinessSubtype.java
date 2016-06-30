package com.devabit.takestock.data.model;

import java.util.Objects;

/**
 * Created by Victor Artemyev on 22/06/2016.
 */
public class BusinessSubtype {

    private int mId;
    private String mName;

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

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusinessSubtype subtype = (BusinessSubtype) o;
        return mId == subtype.mId &&
                Objects.equals(mName, subtype.mName);
    }

    @Override public int hashCode() {
        return Objects.hash(mId, mName);
    }

    @Override public String toString() {
        return "BusinessSubtype{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                '}';
    }
}
