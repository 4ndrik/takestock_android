package com.devabit.takestock.screen.payment;

import android.support.annotation.NonNull;
import com.devabit.takestock.BuildConfig;
import com.devabit.takestock.data.model.Offer;
import com.devabit.takestock.data.model.Payment;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.rx.RxTransformers;
import com.stripe.android.model.Card;
import com.stripe.android.util.TextUtils;
import com.stripe.model.Token;
import com.stripe.net.RequestOptions;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;
import static com.stripe.android.model.Card.*;
import static com.stripe.android.util.TextUtils.hasAnyPrefix;
import static timber.log.Timber.d;
import static timber.log.Timber.e;

/**
 * Created by Victor Artemyev on 04/07/2016.
 */
final class PayByCardPresenter implements PayByCardContract.Presenter {

    private final DataRepository mDataRepository;

    private final PayByCardContract.View mPaymentView;

    private CompositeSubscription mSubscriptions;

    PayByCardPresenter(@NonNull DataRepository dataRepository, @NonNull PayByCardContract.View paymentView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mPaymentView = checkNotNull(paymentView, "paymentView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mPaymentView.setPresenter(PayByCardPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void loadPaymentRate() {
        mPaymentView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.getPaymentRate()
                .compose(RxTransformers.<Integer>applyObservableSchedulers())
                .subscribe(new Subscriber<Integer>() {
                    @Override public void onCompleted() {
                        mPaymentView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        mPaymentView.setProgressIndicator(false);
                        handleError(e);
                    }

                    @Override public void onNext(Integer rate) {
                        mPaymentView.showPaymentRateInView(rate);
                    }
                });
        mSubscriptions.add(subscription);
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

    @Override public void makePayment(Offer offer, Card card) {
        if (!validateCard(card)) return;
        mPaymentView.setProgressIndicator(true);
        Subscription subscription = buildPaymentObservable(offer.getId(), card)
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
                        mPaymentView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        mPaymentView.setProgressIndicator(false);
                        handleError(e);
                    }

                    @Override public void onNext(Offer offer) {
                        mPaymentView.showOfferPaidInView(offer);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private Observable<Payment> buildPaymentObservable(final int offerId, final Card card) {
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
                .map(new Func1<Token, Payment>() {
                    @Override public Payment call(Token token) {
                        d(token.toString());
                        return new Payment(offerId, token.getId(), Payment.Type.CARD);
                    }
                })
                .flatMap(new Func1<Payment, Observable<Payment>>() {
                    @Override public Observable<Payment> call(Payment payment) {
                        return mDataRepository.makePayment(payment);
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

    private void handleError(Throwable throwable) {
        e(throwable);
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
