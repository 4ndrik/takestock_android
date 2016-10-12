package com.devabit.takestock.screen.advert.active.fragment.questions;

import com.devabit.takestock.data.model.Answer;
import com.devabit.takestock.data.model.Question;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
interface QuestionsContract {

    interface View extends BaseView<Presenter> {

        void showQuestionsInView(List<Question> questions);

        void showQuestionRepliedInView(Question question);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void loadQuestions();

        void makeAnswer(Question question, Answer answer);
    }
}
