package com.devabit.takestock.ui.entry.fragments.signUp;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.devabit.takestock.data.model.AuthToken;
import com.devabit.takestock.data.model.UserCredentials;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exceptions.HttpResponseException;
import com.devabit.takestock.exceptions.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import com.devabit.takestock.util.Validator;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.util.Logger.LOGD;
import static com.devabit.takestock.util.Logger.LOGE;
import static com.devabit.takestock.util.Logger.makeLogTag;
import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 06/05/2016.
 */
public class SignUpPresenter implements SignUpContract.Presenter {

    private static final String TAG = makeLogTag(SignUpPresenter.class);

    private final DataRepository mDataRepository;
    private final SignUpContract.View mSignUpView;

    private CompositeSubscription mSubscriptions;

    public SignUpPresenter(@NonNull DataRepository dataRepository, @NonNull SignUpContract.View signUpView) {
        mDataRepository = checkNotNull(dataRepository);
        mSignUpView = checkNotNull(signUpView);
        mSubscriptions = new CompositeSubscription();
        mSignUpView.setPresenter(SignUpPresenter.this);
    }

    @Override public void create() {

    }

    @Override public void resume() {

    }

    @Override public void obtainAccessToken(UserCredentials userCredentials) {
        if (!isUserCredentialsValid(userCredentials)) return;
        mSignUpView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .obtainAuthTokenPerSignUp(userCredentials)
                .compose(RxTransformers.<AuthToken>applyObservableSchedulers())
                .subscribe(new Subscriber<AuthToken>() {
                    @Override public void onCompleted() {
                        mSignUpView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mSignUpView.setProgressIndicator(false);
                        if (e instanceof NetworkConnectionException) {
                            mSignUpView.showNetworkConnectionError();
                        } else if (e instanceof HttpResponseException) {
                            mSignUpView.showIncorrectCredentialsError();
                        } else {
                            mSignUpView.showUnknownError();
                        }
                    }

                    @Override public void onNext(AuthToken authToken) {
                        LOGD(TAG, "Token: " + authToken);
                        mSignUpView.processAuthToken(authToken);
                    }
                });
        mSubscriptions.add(subscription);

    }

    private boolean isUserCredentialsValid(UserCredentials userCredentials) {
        return validateUserName(userCredentials.userName)
                && validateEmailAddress(userCredentials.emailAddress)
                && validatePassword(userCredentials.password);
    }

    private boolean validateUserName(String userName) {
        boolean isValid = Validator.validateUserName(userName);
        if (isValid) return true;
        mSignUpView.showIncorrectUserNameError();
        return false;
    }

    private boolean validateEmailAddress(String emailAddress) {
        boolean isValid = Validator.validateUserName(emailAddress);
        if (isValid) return true;
        mSignUpView.showIncorrectEmailAddressError();
        return false;
    }

    private boolean validatePassword(String password) {
        boolean isValid = !TextUtils.isEmpty(password);
        if(isValid) return true;
        mSignUpView.showIncorrectPasswordError();
        return false;
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
