package com.devabit.takestock;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.devabit.takestock.data.model.AuthToken;

/**
 * Created by Victor Artemyev on 31/08/2016.
 */
public class TakeStockAccount {

    private static final String ACCOUNT_TYPE = "com.devabit.takestock.ACCOUNT";
    private static final String TOKEN_TYPE = "com.devabit.takestock.TOKEN";
    private static final String USER_ID = "com.devabit.takestock.USER_ID";
    private static final String USER_NAME = "com.devabit.takestock.USER_NAME";
    private static final String USER_EMAIL = "com.devabit.takestock.USER_EMAIL";
    private static final String USER_PASSWORD = "com.devabit.takestock.USER_PASSWORD";

    private static volatile TakeStockAccount sInstance;

    public static TakeStockAccount get(Context context) {
        if (sInstance == null) {
            synchronized (TakeStockAccount.class) {
                sInstance = new TakeStockAccount(context);
            }
        }
        return sInstance;
    }

    private final AccountManager mAccountManager;

    @Nullable private Account mAccount;

    private TakeStockAccount(Context context) {
        mAccountManager = AccountManager.get(context);
        mAccount = getCurrentAccountOrNull(mAccountManager);
    }

    @Nullable private Account getCurrentAccountOrNull(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        return accounts.length > 0 ? accounts[0] : null;
    }

    public void createAccount(AuthToken authToken, String password) {
        mAccount = new Account(authToken.email, ACCOUNT_TYPE);
        Bundle userData = new Bundle();
        userData.putString(USER_ID, String.valueOf(authToken.userId));
        userData.putString(USER_NAME, authToken.username);
        userData.putString(USER_EMAIL, authToken.email);
        userData.putString(USER_PASSWORD, password);
        mAccountManager.addAccountExplicitly(mAccount, null, userData);
        setAccessToken(authToken.token);
    }

    public void setAccessToken(String token) {
        if (mAccount == null) return;
        mAccountManager.setAuthToken(mAccount, TOKEN_TYPE, token);
    }

    public String getAccessToken() {
        if (mAccount == null) return "";
        return mAccountManager.peekAuthToken(mAccount, TOKEN_TYPE);
    }

    public int getUserId() {
        if (mAccount == null) return -1;
        String id = mAccountManager.getUserData(mAccount, USER_ID);
        return Integer.valueOf(id);
    }

    public String getUserName() {
        if (mAccount == null) return "";
        return mAccountManager.getUserData(mAccount, USER_NAME);
    }

    public String getUserEmail() {
        if (mAccount == null) return "";
        return mAccountManager.getUserData(mAccount, USER_EMAIL);
    }

    public String getUserPassword() {
        if (mAccount == null) return "";
        return mAccountManager.getUserData(mAccount, USER_PASSWORD);
    }

    public interface OnAccountRemovedListener {
        void onAccountRemoved(boolean isRemoved);
    }

    public void removeAccount(final OnAccountRemovedListener accountRemovedListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            boolean isRemoved = mAccountManager.removeAccountExplicitly(mAccount);
            accountRemovedListener.onAccountRemoved(isRemoved);
            if (isRemoved) {
                mAccount = null;
            }
        } else {
            mAccountManager.removeAccount(mAccount, new AccountManagerCallback<Boolean>() {
                @Override public void run(AccountManagerFuture<Boolean> future) {
                    try {
                        boolean isRemoved = future.getResult();
                        accountRemovedListener.onAccountRemoved(isRemoved);
                        if (isRemoved) {
                            mAccount = null;
                        }
                    } catch (Exception e) {
                        Log.e("TakeStockAccount", "removeAccount", e);
                    }
                }
            }, null);
        }
    }

    public boolean lacksAccount() {
        return mAccount == null;
    }
}