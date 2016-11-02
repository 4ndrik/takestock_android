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
import com.devabit.takestock.data.model.Authentication;
import com.devabit.takestock.data.model.User;


/**
 * Created by Victor Artemyev on 31/08/2016.
 */
public class TakeStockAccount {

    private static final String ACCOUNT_TYPE = "com.devabit.takestock.ACCOUNT";
    private static final String TOKEN_TYPE = "com.devabit.takestock.TOKEN";
    private static final String ID = "com.devabit.takestock.ID";
    private static final String NAME = "com.devabit.takestock.NAME";
    private static final String EMAIL = "com.devabit.takestock.EMAIL";
    private static final String PASSWORD = "com.devabit.takestock.PASSWORD";
    private static final String PHOTO = "com.devabit.takestock.PHOTO";
    private static final String RATING = "com.devabit.takestock.RATING";
    private static final String IS_VERIFIED = "com.devabit.takestock.IS_VERIFIED";
    private static final String IS_VERIFIED_BY_STAFF = "com.devabit.takestock.IS_VERIFIED_BY_STAFF";
    private static final String IS_SUBSCRIBED = "com.devabit.takestock.IS_SUBSCRIBED";
    private static final String BUSINESS_TYPE_ID = "com.devabit.takestock.BUSINESS_TYPE";
    private static final String BUSINESS_SUBTYPE_ID = "com.devabit.takestock.BUSINESS_SUBTYPE";
    private static final String POSTCODE = "com.devabit.takestock.POSTCODE";
    private static final String BUSINESS_NAME = "com.devabit.takestock.BUSINESS_NAME";
    private static final String VAT_NUMBER = "com.devabit.takestock.VAT_NUMBER";

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

    public void createAccount(Authentication authentication, String password) {
        mAccount = new Account(authentication.getEmail(), ACCOUNT_TYPE);
        Bundle userData = new Bundle();
        userData.putString(ID, String.valueOf(authentication.getUserId()));
        userData.putString(NAME, authentication.getUsername());
        userData.putString(EMAIL, authentication.getEmail());
        userData.putString(PASSWORD, password);
        User user = authentication.getUser();
        userData.putString(PHOTO, user == null ? "" : user.getPhoto());
        userData.putString(RATING, user == null ? "0.0" : String.valueOf(user.getAvgRating()));
        userData.putString(IS_VERIFIED, user == null ? "false" : String.valueOf(user.isVerified()));
        userData.putString(IS_VERIFIED_BY_STAFF, user == null ? "false" : String.valueOf(user.isVerifiedByStaffMember()));
        userData.putString(IS_SUBSCRIBED, user == null ? "false" : String.valueOf(user.isSubscribed()));
        userData.putString(BUSINESS_TYPE_ID, user == null ? "0" : String.valueOf(user.getBusinessTypeId()));
        userData.putString(BUSINESS_SUBTYPE_ID, user == null ? "0" : String.valueOf(user.getBusinessSubtypeId()));
        userData.putString(POSTCODE, user == null ? "" : user.getPostcode());
        userData.putString(BUSINESS_NAME, user == null ? "" : user.getBusinessName());
        userData.putString(VAT_NUMBER, user == null ? "" : user.getVatNumber());
        mAccountManager.addAccountExplicitly(mAccount, null, userData);
        setAccessToken(authentication.getToken());
    }

    public void refreshAccount(@Nullable User user) {
        if (lacksAccount() || user == null) return;
        if (user.getId() != getId()) return;
        mAccountManager.setUserData(mAccount, NAME, user.getUserName());
        mAccountManager.setUserData(mAccount, EMAIL, user.getEmail());
        mAccountManager.setUserData(mAccount, PHOTO, user.getPhoto());
        mAccountManager.setUserData(mAccount, RATING, String.valueOf(user.getAvgRating()));
        mAccountManager.setUserData(mAccount, IS_VERIFIED, String.valueOf(user.isVerified()));
        mAccountManager.setUserData(mAccount, IS_VERIFIED_BY_STAFF, String.valueOf(user.isVerifiedByStaffMember()));
        mAccountManager.setUserData(mAccount, IS_SUBSCRIBED, String.valueOf(user.isSubscribed()));
        mAccountManager.setUserData(mAccount, BUSINESS_TYPE_ID, String.valueOf(user.getBusinessTypeId()));
        mAccountManager.setUserData(mAccount, BUSINESS_SUBTYPE_ID, String.valueOf(user.getBusinessSubtypeId()));
        mAccountManager.setUserData(mAccount, POSTCODE, user.getPostcode());
        mAccountManager.setUserData(mAccount, BUSINESS_NAME, user.getBusinessName());
        mAccountManager.setUserData(mAccount, VAT_NUMBER, user.getVatNumber());
    }

    public void setAccessToken(String token) {
        if (lacksAccount()) return;
        mAccountManager.setAuthToken(mAccount, TOKEN_TYPE, token);
    }

    public String getAccessToken() {
        if (lacksAccount()) return "";
        return mAccountManager.peekAuthToken(mAccount, TOKEN_TYPE);
    }

    @Nullable public User getUser() {
        if (lacksAccount()) return null;
        return new User.Builder()
                .setId(getId())
                .setUserName(getName())
                .setEmail(getEmail())
                .setPhoto(getPhoto())
                .setAvgRating(getRating())
                .setIsVerified(isVerified())
                .setVerifiedByStaffMember(isVerifiedByStaff())
                .setIsSubscribed(isSubscribed())
                .setBusinessName(getBusinessName())
                .setPostcode(getPostcode())
                .setBusinessTypeId(getBusinessTypeId())
                .setBusinessSubtypeId(getBusinessSubtypeId())
                .setVatNumber(getVatNumber())
                .build();
    }

    public int getId() {
        if (lacksAccount()) return -1;
        String id = mAccountManager.getUserData(mAccount, ID);
        return Integer.valueOf(id);
    }

    public String getName() {
        if (lacksAccount()) return "";
        return mAccountManager.getUserData(mAccount, NAME);
    }

    public String getEmail() {
        if (lacksAccount()) return "";
        return mAccountManager.getUserData(mAccount, EMAIL);
    }

    public String getPassword() {
        if (lacksAccount()) return "";
        return mAccountManager.getUserData(mAccount, PASSWORD);
    }

    public void setPassword(String password) {
        if (lacksAccount()) return;
        mAccountManager.setUserData(mAccount, PASSWORD, password);
    }

    public String getPhoto() {
        if (lacksAccount()) return "";
        return mAccountManager.getUserData(mAccount, PHOTO);
    }

    public float getRating() {
        if (lacksAccount()) return 0f;
        String rating = mAccountManager.getUserData(mAccount, RATING);
        return rating == null ? 0f : Float.valueOf(rating);
    }

    public boolean isVerified() {
        if(lacksAccount()) return false;
        String isVerified = mAccountManager.getUserData(mAccount, IS_VERIFIED);
        return isVerified == null ? false : Boolean.valueOf(isVerified);
    }

    public boolean isVerifiedByStaff() {
        if(lacksAccount()) return false;
        String isVerified = mAccountManager.getUserData(mAccount, IS_VERIFIED_BY_STAFF);
        return isVerified == null ? false : Boolean.valueOf(isVerified);
    }

    public boolean isSubscribed() {
        if(lacksAccount()) return false;
        String isSubscribed = mAccountManager.getUserData(mAccount, IS_SUBSCRIBED);
        return isSubscribed == null ? false : Boolean.valueOf(isSubscribed);
    }

    public int getBusinessTypeId() {
        if (lacksAccount()) return -1;
        String id = mAccountManager.getUserData(mAccount, BUSINESS_TYPE_ID);
        return Integer.valueOf(id);
    }

    public int getBusinessSubtypeId() {
        if (lacksAccount()) return -1;
        String id = mAccountManager.getUserData(mAccount, BUSINESS_SUBTYPE_ID);
        return Integer.valueOf(id);
    }

    public String getPostcode() {
        if (lacksAccount()) return "";
        return mAccountManager.getUserData(mAccount, POSTCODE);
    }

    public String getBusinessName() {
        if (lacksAccount()) return "";
        return mAccountManager.getUserData(mAccount, BUSINESS_NAME);
    }

    public String getVatNumber() {
        if (lacksAccount()) return "";
        return mAccountManager.getUserData(mAccount, VAT_NUMBER);
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
