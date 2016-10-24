package com.devabit.takestock.screen.payment.byBACS;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Payment;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;
import static timber.log.Timber.e;

/**
 * Created by Victor Artemyev on 04/07/2016.
 */
final class PayByBACSPresenter implements PayByBACSContract.Presenter {

    private final DataRepository mDataRepository;
    private final PayByBACSContract.View mView;

    private CompositeSubscription mSubscriptions;

    PayByBACSPresenter(@NonNull DataRepository dataRepository, @NonNull PayByBACSContract.View view) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mView = checkNotNull(view, "view cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(PayByBACSPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void makePayment(Payment payment) {
        mView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.makePayment(payment)
                .compose(RxTransformers.<Payment>applyObservableSchedulers())
                .subscribe(new Subscriber<Payment>() {
                    @Override public void onCompleted() {
                        mView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        mView.setProgressIndicator(false);
                        handleError(e);
                    }

                    @Override public void onNext(Payment payment) {
                        mView.showPaymentMadeInView(payment);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void handleError(Throwable throwable) {
        e(throwable);
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
