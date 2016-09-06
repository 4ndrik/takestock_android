package com.devabit.takestock.data.model;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by Victor Artemyev on 13/05/2016.
 */
public class PaginatedList<T> {

    private int mCount;
    private String mNext;
    private String mPrevious;
    private List<T> mResults;

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public String getNext() {
        return mNext;
    }

    public void setNext(String next) {
        mNext = next;
    }

    public boolean hasNext() {
        return !TextUtils.isEmpty(mNext);
    }

    public String getPrevious() {
        return mPrevious;
    }

    public void setPrevious(String previous) {
        mPrevious = previous;
    }

    public boolean hasPrevious() {
        return !TextUtils.isEmpty(mPrevious);
    }

    public List<T> getResults() {
        return mResults;
    }

    public void setResults(List<T> results) {
        mResults = results;
    }

    @Override public String toString() {
        return "ResultList{" +
                "mCount=" + mCount +
                ", mNext='" + mNext + '\'' +
                ", mPrevious='" + mPrevious + '\'' +
                ", mResults=" + mResults +
                '}';
    }
}
