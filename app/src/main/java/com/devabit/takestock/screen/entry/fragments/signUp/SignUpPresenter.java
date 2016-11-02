package com.devabit.takestock.screen.entry.fragments.signUp;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.devabit.takestock.data.model.Authentication;
import com.devabit.takestock.data.model.Device;
import com.devabit.takestock.data.model.UserCredentials;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.HttpResponseException;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import com.devabit.takestock.utils.Validator;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Logger.makeLogTag;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

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

    @Override public void resume() {

    }

    @Override public void signUp(UserCredentials userCredentials) {
        if (!isUserCredentialsValid(userCredentials)) return;
        mSignUpView.setProgressIndicator(true);
        Subscription subscription = mDataRepository
                .signUp(userCredentials)
                .flatMap(new Func1<Authentication, Observable<Authentication>>() {
                    @Override public Observable<Authentication> call(Authentication authentication) {
                        final Device device = mSignUpView.getDevice();
                        device.setUserId(authentication.getUserId());
                        return Observable.zip(
                                Observable.just(authentication),
                                mDataRepository.registerDevice(device),
                                new Func2<Authentication, Boolean, Authentication>() {
                                    @Override public Authentication call(Authentication authentication, Boolean isDeviceReg) {
                                        Timber.d("%s registered - %s", device, isDeviceReg);
                                        return authentication;
                                    }
                                });
                    }
                })
                .compose(RxTransformers.<Authentication>applyObservableSchedulers())
                .subscribe(new Subscriber<Authentication>() {
                    @Override public void onCompleted() {
                        mSignUpView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "BOOM:", e);
                        mSignUpView.setProgressIndicator(false);
                        if (e instanceof NetworkConnectionException) {
                            mSignUpView.showNetworkConnectionError();
                        } else if (e instanceof HttpResponseException) {
                            String error = ((HttpResponseException) e).getResponse();
                            mSignUpView.showCredentialsError(error);
                        } else {
                            mSignUpView.showUnknownError();
                        }
                    }

                    @Override public void onNext(Authentication authToken) {
                        mSignUpView.showAuthTokenInView(authToken);
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
        mSignUpView.showUserNameError();
        return false;
    }

    private boolean validateEmailAddress(String emailAddress) {
        boolean isValid = Validator.validateEmailAddress(emailAddress);
        if (isValid) return true;
        mSignUpView.showEmailError();
        return false;
    }

    private boolean validatePassword(String password) {
        boolean isValid = !TextUtils.isEmpty(password);
        if(isValid) return true;
        mSignUpView.showPasswordError();
        return false;
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
