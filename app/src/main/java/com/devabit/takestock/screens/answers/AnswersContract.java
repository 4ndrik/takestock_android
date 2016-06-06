package com.devabit.takestock.screens.answers;

import com.devabit.takestock.data.models.Answer;
import com.devabit.takestock.data.models.Question;
import com.devabit.takestock.screens.BasePresenter;
import com.devabit.takestock.screens.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public interface AnswersContract {

    interface View extends BaseView<Presenter> {

        void showQuestionsInView(List<Question> questions);

        void showAnswerInView(Answer answer);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void fetchQuestionsByAdvertId(int advertId);

        void makeAnswer(Answer answer);
    }
}
