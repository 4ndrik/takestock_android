package com.devabit.takestock.screen.payment.byBACS;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Payment;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.rx.RxTransformers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
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
                .filter(new Func1<Payment, Boolean>() {
                    @Override public Boolean call(Payment payment) {
                        if (payment.isSuccessful()) return Boolean.TRUE;
                        throw new RuntimeException("Payment failed");
                    }
                })
                .flatMap(new Func1<Payment, Observable<Offer>>() {
                    @Override public Observable<Offer> call(Payment payment) {
                        return mDataRepository.getOfferWithId(payment.getOfferId());
                    }
                })
                .compose(RxTransformers.<Offer>applyObservableSchedulers())
                .subscribe(new Subscriber<Offer>() {
                    @Override public void onCompleted() {
                        mView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        mView.setProgressIndicator(false);
                        mView.showPaymentError();
                        handleError(e);
                    }

                    @Override public void onNext(Offer offer) {
                        mView.showOfferPaidInView(offer);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void handleError(Throwable throwable) {
        e(throwable);
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
