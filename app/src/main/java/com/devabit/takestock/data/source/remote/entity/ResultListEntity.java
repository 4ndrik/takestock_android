package com.devabit.takestock.data.source.remote.entity;

/**
 * Created by Victor Artemyev on 10/05/2016.
 */
public class ResultListEntity {

    private int mCount;
    private String mNext;
    private String mPrevious;
    private String mResults;

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

    public String getPrevious() {
        return mPrevious;
    }

    public void setPrevious(String previous) {
        mPrevious = previous;
    }

    public String getResults() {
        return mResults;
    }

    public void setResults(String results) {
        mResults = results;
    }

    @Override public String toString() {
        return "Response{" +
                "mCount=" + mCount +
                ", mNext='" + mNext + '\'' +
                ", mPrevious='" + mPrevious + '\'' +
                ", mResults='" + mResults + '\'' +
                '}';
    }
}
