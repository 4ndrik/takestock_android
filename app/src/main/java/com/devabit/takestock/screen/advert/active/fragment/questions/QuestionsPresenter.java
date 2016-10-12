package com.devabit.takestock.screen.advert.active.fragment.questions;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.filter.QuestionFilter;
import com.devabit.takestock.data.model.Answer;
import com.devabit.takestock.data.model.PaginatedList;
import com.devabit.takestock.data.model.Question;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
class QuestionsPresenter implements QuestionsContract.Presenter {

    private final int mAdvertId;
    private final DataRepository mDataRepository;
    private final QuestionsContract.View mView;
    private final CompositeSubscription mSubscriptions;

    private PaginatedList<Question> mPaginatedList;

    QuestionsPresenter(int advertId, @NonNull DataRepository dataRepository, @NonNull QuestionsContract.View view) {
        mAdvertId = advertId;
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(QuestionsPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void loadQuestions() {
        Observable<PaginatedList<Question>> observable = null;
        if (mPaginatedList == null) {
            observable = mDataRepository.getPaginatedQuestionListWithFilter(createFilter());
        } else if (mPaginatedList.hasNext()) {
            observable = mDataRepository.getPaginatedQuestionListPerPage(mPaginatedList.getNext());
        }
        if (observable == null) return;
        mView.setProgressIndicator(true);
        Subscription subscription = observable
                .compose(RxTransformers.<PaginatedList<Question>>applyObservableSchedulers())
                .subscribe(new Subscriber<PaginatedList<Question>>() {
                    @Override public void onCompleted() {
                        mView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable throwable) {
                        mView.setProgressIndicator(false);
                        handleError(throwable);
                    }

                    @Override public void onNext(PaginatedList<Question> paginatedList) {
                        mPaginatedList = paginatedList;
                        mView.showQuestionsInView(mPaginatedList.getResults());
                    }
                });
        mSubscriptions.add(subscription);

    }

    private QuestionFilter createFilter() {
        return new QuestionFilter.Builder()
                .setAdvertId(mAdvertId)
                .setOrder(QuestionFilter.Order.CREATED_AT_DESCENDING)
                .create();
    }

    @Override public void makeAnswer(final Question question, Answer answer) {
        mView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .saveAnswer(answer)
                .compose(RxTransformers.<Answer>applyObservableSchedulers())
                .subscribe(new Subscriber<Answer>() {
                    @Override public void onCompleted() {
                        mView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable throwable) {
                        mView.setProgressIndicator(false);
                        handleError(throwable);
                    }

                    @Override public void onNext(Answer answer) {
                        question.setAnswer(answer);
                        mView.showQuestionRepliedInView(question);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable);
        if (throwable instanceof NetworkConnectionException) {
            mView.showNetworkConnectionError();
        } else {
            mView.showUnknownError();
        }
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
