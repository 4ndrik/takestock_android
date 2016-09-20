package com.devabit.takestock.data.filter;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class QuestionFilter extends Filter {

    private final int mAdvertId;

    public QuestionFilter(int advertId) {
        mAdvertId = advertId;
    }

    public int getAdvertId() {
        return mAdvertId;
    }
}
