package com.devabit.takestock.screen.payment;

import android.support.annotation.NonNull;
import com.devabit.takestock.BuildConfig;
import com.devabit.takestock.data.model.Payment;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import com.stripe.android.model.Card;
import com.stripe.android.util.TextUtils;
import com.stripe.model.Token;
import com.stripe.net.RequestOptions;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.devabit.takestock.utils.Logger.*;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;
import static com.stripe.android.model.Card.*;
import static com.stripe.android.util.TextUtils.hasAnyPrefix;

/**
 * Created by Victor Artemyev on 04/07/2016.
 */
final class PaymentPresenter implements PaymentContract.Presenter {

    private static final String TAG = makeLogTag(PaymentPresenter.class);

    private final DataRepository mDataRepository;

    private final PaymentContract.View mPaymentView;

    private CompositeSubscription mSubscriptions;

    PaymentPresenter(@NonNull DataRepository dataRepository, @NonNull PaymentContract.View paymentView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mPaymentView = checkNotNull(paymentView, "paymentView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mPaymentView.setPresenter(PaymentPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void validateCardNumber(String number) {
        if (number.isEmpty()) {
            mPaymentView.showCardUnknownInView();
            return;
        }

        if (hasAnyPrefix(number, PREFIXES_VISA)) {
            mPaymentView.showCardVisaInView();
        } else if (hasAnyPrefix(number, PREFIXES_MASTERCARD)) {
            mPaymentView.showCardMastercardInView();
        } else if (hasAnyPrefix(number, PREFIXES_AMERICAN_EXPRESS)) {
            mPaymentView.showCardAmericanExpressInView();
        } else if (hasAnyPrefix(number, PREFIXES_DISCOVER)) {
            mPaymentView.showCardDiscoverInView();
        } else if (hasAnyPrefix(number, PREFIXES_JCB)) {
            mPaymentView.showCardJCBInView();
        } else if (hasAnyPrefix(number, PREFIXES_DINERS_CLUB)) {
            mPaymentView.showCardDinnersClubInView();
        } else {
            mPaymentView.showCardUnknownInView();
        }
    }

    private boolean validateCard(Card card) {
        return validateCardNumber(card)
                && validateCardExpiryDate(card)
                && validateCardCVVCode(card);
    }

    private boolean validateCardNumber(Card card) {
        if (card.validateNumber()) {
            return true;
        }
        mPaymentView.showCardNumberError();
        return false;
    }

    private boolean validateCardExpiryDate(Card card) {
        if (card.validateExpiryDate()) {
            return true;
        }
        mPaymentView.showExpiryDateError();
        return false;
    }

    private boolean validateCardCVVCode(Card card) {
        if (card.validateCVC()) {
            return true;
        }
        mPaymentView.showCVVCodeError();
        return false;
    }

    @Override public void makePayment(int offerId, Card card) {
        LOGD(TAG, card);
        if (!validateCard(card)) return;
        mPaymentView.setProgressIndicator(true);
        Subscription subscription = buildPaymentObservable(offerId, card)
                .compose(RxTransformers.<String>applyObservableSchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override public void onCompleted() {
                        mPaymentView.setProgressIndicator(false);
                        mPaymentView.showUnknownError();
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mPaymentView.setProgressIndicator(false);
                        if (e instanceof NetworkConnectionException) {
                            mPaymentView.showNetworkConnectionError();
                        } else {
                            mPaymentView.showUnknownError();
                        }
                    }

                    @Override public void onNext(String s) {
                        LOGD(TAG, s);
                    }
                });
        mSubscriptions.add(subscription);

    }

    private Observable<String> buildPaymentObservable(final int offerId, final Card card) {
        return Observable.just(card)
                .map(new Func1<Card, Token>() {
                    @Override public Token call(Card card) {
                        RequestOptions requestOptions = RequestOptions.builder()
                                .setApiKey(BuildConfig.STRIPE_API_KEY).build();
                        try {
                            return Token.create(getTokenRequestParamsFromCard(card), requestOptions);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .doOnNext(new Action1<Token>() {
                    @Override public void call(Token token) {
                        LOGD(TAG, token);
                    }
                })
                .map(new Func1<Token, Payment>() {
                    @Override public Payment call(Token token) {
                        Payment payment = new Payment();
                        payment.setOfferId(offerId);
                        payment.setTokenId(token.getId());
                        return payment;
                    }
                })
                .flatMap(new Func1<Payment, Observable<String>>() {
                    @Override public Observable<String> call(Payment payment) {
                        return mDataRepository.addPayment(payment);
                    }
                });
    }

    private Map<String, Object> getTokenRequestParamsFromCard(Card card) {
        Map<String, Object> tokenParams = new HashMap<>();

        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put("number", TextUtils.nullIfBlank(card.getNumber()));
        cardParams.put("cvc", TextUtils.nullIfBlank(card.getCVC()));
        cardParams.put("exp_month", card.getExpMonth());
        cardParams.put("exp_year", card.getExpYear());
        cardParams.put("name", TextUtils.nullIfBlank(card.getName()));
        cardParams.put("currency", TextUtils.nullIfBlank(card.getCurrency()));
        cardParams.put("address_line1", TextUtils.nullIfBlank(card.getAddressLine1()));
        cardParams.put("address_line2", TextUtils.nullIfBlank(card.getAddressLine2()));
        cardParams.put("address_city", TextUtils.nullIfBlank(card.getAddressCity()));
        cardParams.put("address_zip", TextUtils.nullIfBlank(card.getAddressZip()));
        cardParams.put("address_state", TextUtils.nullIfBlank(card.getAddressState()));
        cardParams.put("address_country", TextUtils.nullIfBlank(card.getAddressCountry()));

        // Remove all null values; they cause validation errors
        for (String key : new HashSet<>(cardParams.keySet())) {
            if (cardParams.get(key) == null) {
                cardParams.remove(key);
            }
        }

        tokenParams.put("card", cardParams);
        return tokenParams;
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
