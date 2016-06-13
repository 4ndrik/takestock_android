package com.devabit.takestock.screens.answers;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filters.QuestionFilter;
import com.devabit.takestock.data.models.Answer;
import com.devabit.takestock.data.models.Question;
import com.devabit.takestock.data.models.ResultList;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exceptions.NetworkConnectionException;
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
public class AnswersPresenter implements AnswersContract.Presenter {

    private static final String TAG = makeLogTag(AnswersPresenter.class);

    private final DataRepository mDataRepository;
    private final AnswersContract.View mAnswersView;

    private CompositeSubscription mSubscriptions;

    public AnswersPresenter(@NonNull DataRepository dataRepository, @NonNull AnswersContract.View answersView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mAnswersView = checkNotNull(answersView, "answersView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mAnswersView.setPresenter(AnswersPresenter.this);
    }

    @Override public void create() {

    }

    @Override public void resume() {

    }

    @Override public void fetchQuestionsByAdvertId(int advertId) {
        mAnswersView.setProgressIndicator(true);
        QuestionFilter filter = new QuestionFilter();
        filter.setAdvertId(advertId);
        Subscription subscription = mDataRepository
                .getQuestionResultListPerFilter(filter)
                .compose(RxTransformers.<ResultList<Question>>applyObservableSchedulers())
                .subscribe(new Action1<ResultList<Question>>() {
                    @Override public void call(ResultList<Question> resultList) {
                        mAnswersView.showQuestionsInView(resultList.getResults());
                    }
                }, getOnError(), getOnCompleted());
        mSubscriptions.add(subscription);
    }

    @Override public void makeAnswer(Answer answer) {
        mAnswersView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .saveAnswer(answer)
                .compose(RxTransformers.<Answer>applyObservableSchedulers())
                .subscribe(new Action1<Answer>() {
                    @Override public void call(Answer answer) {
                        mAnswersView.showAnswerInView(answer);
                    }
                }, getOnError(), getOnCompleted());
        mSubscriptions.add(subscription);
    }

    @NonNull private Action1<Throwable> getOnError() {
        return new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                mAnswersView.setProgressIndicator(false);
                LOGE(TAG, "BOOM:", throwable);
                if (throwable instanceof NetworkConnectionException) {
                    mAnswersView.showNetworkConnectionError();
                } else {
                    mAnswersView.showUnknownError();
                }
            }
        };
    }

    @NonNull private Action0 getOnCompleted() {
        return new Action0() {
            @Override public void call() {
                mAnswersView.setProgressIndicator(false);
            }
        };
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
