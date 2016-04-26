package com.devabit.takestock.ui.signIn;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.devabit.takestock.data.DataRepository;
import com.devabit.takestock.data.model.AccessToken;
import com.devabit.takestock.data.model.UserCredentials;
import com.devabit.takestock.data.shared.AuthConstants;
import com.devabit.takestock.rx.RxTransformers;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.devabit.takestock.util.Logger.LOGD;
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

    @Override public void signIn() {
        if (isAllValid()) {
            obtainAccessToken();
        } else {
            mSignInView.showErrorMessage("No valid credentials");
        }
    }

    private void obtainAccessToken() {
        mSignInView.setProgressIndicator(true);
        mSubscriptions.clear();

        String name = mSignInView.getUserName();
        String password = mSignInView.getPassword();
        final UserCredentials credentials = new UserCredentials(name, password);
        Subscription subscription = mDataRepository.obtainAccessToken(credentials)
                .compose(RxTransformers.<AccessToken>applyObservableSchedulers())
                .subscribe(new Subscriber<AccessToken>() {
                    @Override public void onCompleted() {
                        mSignInView.setProgressIndicator(false);
                    }

                    @Override public void onError(Throwable e) {
                        LOGE(TAG, "onError", e);
                        mSignInView.failSignIn();
                    }

                    @Override public void onNext(AccessToken accessToken) {
                        AccountManager accountManager = mSignInView.getAccountManager();
                        Account account = new Account(credentials.name, AuthConstants.ACCOUNT_TYPE);
                        accountManager.addAccountExplicitly(account, credentials.password, null);
                        accountManager.setAuthToken(account, AuthConstants.AUTH_TOKEN_TYPE, accessToken.token);

                        Account[] accounts = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
                        for (Account ac : accounts) {
                            LOGD(TAG, ac.toString());
                            LOGD(TAG, accountManager.peekAuthToken(ac, AuthConstants.AUTH_TOKEN_TYPE));
                        }

                        mSignInView.successSignIn();
                    }
                });
        mSubscriptions.add(subscription);
    }

    private boolean isAllValid() {
        return validateUserName() && validatePassword();
    }

    private boolean validateUserName() {
        return !TextUtils.isEmpty(mSignInView.getUserName());
    }

    private boolean validatePassword() {
        return !TextUtils.isEmpty(mSignInView.getPassword());
    }

    @Override public void unsubscribe() {
        mSubscriptions.clear();
    }
}
