package com.devabit.takestock.screen.questions;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.QuestionFilter;
import com.devabit.takestock.data.model.Question;
import com.devabit.takestock.data.model.PaginatedList;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Logger.makeLogTag;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public class QuestionsPresenter implements QuestionsContract.Presenter {

    private static final String TAG = makeLogTag(QuestionsPresenter.class);

    private final DataRepository mDataRepository;
    private final QuestionsContract.View mQuestionView;

    private CompositeSubscription mSubscriptions;

    public QuestionsPresenter(@NonNull DataRepository dataRepository, @NonNull QuestionsContract.View questionView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mQuestionView = checkNotNull(questionView, "questionView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mQuestionView.setPresenter(QuestionsPresenter.this);
    }


    @Override public void resume() {

    }

    @Override public void fetchQuestionsByAdvertId(int advertId) {
        mQuestionView.setProgressIndicator(true);
        QuestionFilter filter = new QuestionFilter();
        filter.setAdvertId(advertId);
        Subscription subscription = mDataRepository
                .getQuestionResultListPerFilter(filter)
                .compose(RxTransformers.<PaginatedList<Question>>applyObservableSchedulers())
                .subscribe(new Action1<PaginatedList<Question>>() {
                    @Override public void call(PaginatedList<Question> resultList) {
                        mQuestionView.showQuestionsInView(resultList.getResults());
                    }
                }, getOnError(), getOnCompleted());
        mSubscriptions.add(subscription);
    }

    @Override public void makeQuestion(Question question) {
        mQuestionView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .saveQuestion(question)
                .compose(RxTransformers.<Question>applyObservableSchedulers())
                .subscribe(new Action1<Question>() {
                    @Override public void call(Question question) {
                        mQuestionView.showQuestionInView(question);
                    }
                }, getOnError(), getOnCompleted());
        mSubscriptions.add(subscription);
    }

    @NonNull private Action1<Throwable> getOnError() {
        return new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                mQuestionView.setProgressIndicator(false);
                LOGE(TAG, "BOOM:", throwable);
                if (throwable instanceof NetworkConnectionException) {
                    mQuestionView.showNetworkConnectionError();
                } else {
                    mQuestionView.showUnknownError();
                }
            }
        };
    }

    @NonNull private Action0 getOnCompleted() {
        return new Action0() {
            @Override public void call() {
                mQuestionView.setProgressIndicator(false);
            }
        };
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
