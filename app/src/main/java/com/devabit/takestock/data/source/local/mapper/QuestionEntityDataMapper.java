package com.devabit.takestock.data.source.local.mapper;

import com.devabit.takestock.data.models.Answer;
import com.devabit.takestock.data.models.Question;
import com.devabit.takestock.data.source.local.entity.AnswerEntity;
import com.devabit.takestock.data.source.local.entity.QuestionEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class QuestionEntityDataMapper {

    private final AnswerEntityDataMapper mAnswerMapper;

    public QuestionEntityDataMapper() {
        mAnswerMapper = new AnswerEntityDataMapper();
    }

    public List<Question> transformFromEntitiesToList(List<QuestionEntity> entities) {
        List<Question> result = new ArrayList<>(entities.size());
        for (QuestionEntity entity : entities) {
            Question question = transformFromEntity(entity);
            result.add(question);
        }
        return result;
    }

    public Question transformFromEntity(QuestionEntity entity) {
        Question question = new Question();
        question.setId(entity.getId());
        question.setUserId(entity.getUserId());
        question.setAdvertId(entity.getAdvertId());
        question.setMessage(entity.getMessage());
        question.setNew(entity.isNew());
        question.setDateCreated(entity.getDateCreated());
        question.setUserName(entity.getUserName());
        if (entity.getAnswer() != null) {
            Answer answer = mAnswerMapper.transformFromEntity(entity.getAnswer());
            question.setAnswer(answer);
        }
        return question;
    }

    public QuestionEntity transformToEntity(Question question) {
        QuestionEntity entity = new QuestionEntity();
        entity.setId(question.getId());
        entity.setUserId(question.getUserId());
        entity.setAdvertId(question.getAdvertId());
        entity.setMessage(question.getMessage());
        entity.setNew(question.isNew());
        entity.setDateCreated(question.getDateCreated());
        entity.setUserName(question.getUserName());
        if (question.getAnswer() != null) {
            AnswerEntity answerEntity = mAnswerMapper.transformToEntity(question.getAnswer());
            entity.setAnswer(answerEntity);
        }
        return entity;
    }
}
