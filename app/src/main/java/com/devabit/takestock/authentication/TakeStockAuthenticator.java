package com.devabit.takestock.authentication;

import android.accounts.*;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Victor Artemyev on 27/04/2016.
 */
public class TakeStockAuthenticator extends AbstractAccountAuthenticator {

    public TakeStockAuthenticator(Context context) {
        super(context);
    }

    @Override public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }

    @Override public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
        return result;
    }
}
