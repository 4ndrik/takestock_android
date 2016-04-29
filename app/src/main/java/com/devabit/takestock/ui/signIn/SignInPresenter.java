package com.devabit.takestock.ui.signIn;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.data.model.AuthToken;
import com.devabit.takestock.data.model.UserCredentials;
import com.devabit.takestock.exceptions.HttpResponseException;
import com.devabit.takestock.exceptions.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.util.Logger.LOGE;
import static com.devabit.takestock.util.Logger.makeLogTag;
import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public class SignInPresenter implements SignInContract.Presenter {

    private static final String TAG = makeLogTag(SignInPresenter.class);

    private final DataRepository mDataRepository;
    private final SignInContract.View mSignInView;

    private CompositeSubscription mSubscriptions;

    public SignInPresenter(@NonNull DataRepository dataRepository, @NonNull SignInContract.View signInView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mSignInView = checkNotNull(signInView, "signInView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mSignInView.setPresenter(SignInPresenter.this);
    }

    @Override public void subscribe() {

    }

    @Override public void obtainAccessToken(String username, String password) {
        if (!validateUsername(username) || !validatePassword(password)) return;

        mSignInView.setProgressIndicator(true);
        mSubscriptions.clear();
        final UserCredentials credentials = new UserCredentials(username, password);
        Subscription subscription = mDataRepository.obtainAuthToken(credentials)
                .compose(RxTransformers.<AuthToken>applyObservableSchedulers())
                .subscribe(new Subscriber<AuthToken>() {
                    @Override public void onCompleted() {
                        mSignInView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "onError", e);
                        mSignInView.setProgressIndicator(false);

                        if (e instanceof NetworkConnectionException) {
                            mSignInView.showNetworkConnectionError();
                        } else if (e instanceof HttpResponseException) {
                            mSignInView.showIncorrectCredentialsError();
                        } else {
                            mSignInView.showUnknownError();
                        }
                    }

                    @Override public void onNext(AuthToken authToken) {
                        mSignInView.createAccount(authToken);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private boolean validateUsername(String name) {
        if(TextUtils.isEmpty(name)) {
            mSignInView.showIncorrectUsernameError();
            return false;
        }

        return true;
    }

    private boolean validatePassword(String password) {
        if(TextUtils.isEmpty(password)) {
            mSignInView.showIncorrectPasswordError();
            return false;
        }

        return true;
    }


    @Override public void unsubscribe() {
        mSubscriptions.clear();
    }
}
