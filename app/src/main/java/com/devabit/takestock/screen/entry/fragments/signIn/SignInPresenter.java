package com.devabit.takestock.screen.entry.fragments.signIn;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Authentication;
import com.devabit.takestock.data.model.Device;
import com.devabit.takestock.data.model.UserCredentials;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.exception.HttpResponseException;
import com.devabit.takestock.exception.NetworkConnectionException;
import com.devabit.takestock.rx.RxTransformers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 25/04/2016.
 */
public class SignInPresenter implements SignInContract.Presenter {

    private final DataRepository mDataRepository;
    private final SignInContract.View mSignInView;

    private CompositeSubscription mSubscriptions;

    public SignInPresenter(@NonNull DataRepository dataRepository, @NonNull SignInContract.View signInView) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null.");
        mSignInView = checkNotNull(signInView, "signInView cannot be null.");
        mSubscriptions = new CompositeSubscription();
        mSignInView.setPresenter(SignInPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void signIn(UserCredentials credentials) {
        if (!isUserCredentialsValid(credentials)) return;
        mSignInView.setProgressIndicator(true);
        Subscription subscription = mDataRepository.signIn(credentials)
                .flatMap(new Func1<Authentication, Observable<Authentication>>() {
                    @Override public Observable<Authentication> call(Authentication authentication) {
                        final Device device = mSignInView.getDevice();
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
                        mSignInView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        Timber.e(e);
                        mSignInView.setProgressIndicator(false);

                        if (e instanceof NetworkConnectionException) {
                            mSignInView.showNetworkConnectionError();
                        } else if (e instanceof HttpResponseException) {
                            mSignInView.showSignInError();
                        } else {
                            mSignInView.showUnknownError();
                        }
                    }

                    @Override public void onNext(Authentication authToken) {
                        mSignInView.showSignInSuccess();
                    }
                });
        mSubscriptions.add(subscription);
    }

    private boolean isUserCredentialsValid(UserCredentials credentials) {
        return validateUsername(credentials.userName)
                && validatePassword(credentials.password);
    }

    private boolean validateUsername(String userName) {
        if (userName.isEmpty()) {
            mSignInView.showUserNameError();
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
       if (password.isEmpty()) {
           mSignInView.showPasswordError();
           return false;
       }
        return true;
    }

    @Override public void pause() {
        mSubscriptions.clear();
    }

    @Override public void destroy() {

    }
}
