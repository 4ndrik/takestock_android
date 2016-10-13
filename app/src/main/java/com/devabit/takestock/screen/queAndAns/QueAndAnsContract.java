package com.devabit.takestock.screen.queAndAns;

import com.devabit.takestock.data.model.Question;
import com.devabit.takestock.screen.BasePresenter;
import com.devabit.takestock.screen.BaseView;

import java.util.List;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public interface QueAndAnsContract {

    interface View extends BaseView<Presenter> {

        void showQuestionsInView(List<Question> questions);

        void showQuestionInView(Question question);

        void showNetworkConnectionError();

        void showUnknownError();

        void setProgressIndicator(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void fetchQuestionsWithAdvertId(int advertId);

        void makeQuestion(Question question);
    }
}
