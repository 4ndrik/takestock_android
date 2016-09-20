package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.PaginatedList;
import com.devabit.takestock.data.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 13/09/2016.
 */
public class PaginatedQuestionListJson implements JsonModel {

    public int count;
    public String next;
    public String previous;
    public List<QuestionJson> results;

    public PaginatedList<Question> toPaginatedList() {
        PaginatedList<Question> list = new PaginatedList<>();
        list.setCount(count);
        list.setNext(next);
        list.setPrevious(previous);
        list.setResults(toQuestionList());
        return list;
    }

    private List<Question> toQuestionList() {
        List<Question> questions = new ArrayList<>(results.size());
        for (QuestionJson json : results) {
            questions.add(json.toQuestion());
        }
        return questions;
    }
}
