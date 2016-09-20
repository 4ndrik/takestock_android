package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Question;

/**
 * Created by Victor Artemyev on 20/09/2016.
 */

public class QuestionCreateJson implements JsonModel {

    public int user;
    public String message;
    public int advert;

    public QuestionCreateJson(Question question) {
        this.user = question.getUserId();
        this.message = question.getMessage();
        this.advert = question.getAdvertId();
    }
}
