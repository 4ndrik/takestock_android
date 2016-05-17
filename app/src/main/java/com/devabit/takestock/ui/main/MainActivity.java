package com.devabit.takestock.ui.main;

import android.accounts.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.R;
import com.devabit.takestock.ui.category.CategoriesDialog;
import com.devabit.takestock.ui.entry.EntryActivity;
import com.devabit.takestock.ui.search.SearchActivity;
import com.devabit.takestock.ui.selling.SellingActivity;
import com.devabit.takestock.util.FontCache;

import java.io.IOException;

import static com.devabit.takestock.util.Logger.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = makeLogTag(MainActivity.class);

    public static Intent getStartIntent(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
//        starter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return starter;
    }

    private static final int REQUEST_CODE_SELLING_ACTIVITY = 101;
    private static final int REQUEST_CODE_PROFILE_ACTIVITY = 102;
    private static final int REQUEST_CODE_NOTIFICATIONS_ACTIVITY = 103;
    private static final int REQUEST_CODE_WATCHING_ACTIVITY = 104;

    @BindView(R.id.drawer_layout) protected DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) protected NavigationView mNavigationView;
    @BindView(R.id.search_products_edit_text) protected EditText mSearchProductsEditText;
    @BindView(R.id.browse_categories_button) protected Button mBrowseProductsButton;
    @BindView(R.id.sell_something_button) protected Button mSellSomethingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(MainActivity.this);
        Typeface mediumTypeface = FontCache.getTypeface(MainActivity.this, R.string.font_brandon_medium);
        mSearchProductsEditText.setTypeface(mediumTypeface);

        Typeface boldTypeface = FontCache.getTypeface(MainActivity.this, R.string.font_brandon_bold);
        mBrowseProductsButton.setTypeface(boldTypeface);
        mSellSomethingButton.setTypeface(boldTypeface);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.nav_profile:
                        onProfileMenuItemClick();
                        return true;

                    case R.id.nav_notifications:
                        onNotificationMenuItemClick();
                        return true;

                    case R.id.nav_watching:
                        onWatchingMenuItemClick();
                        return true;

                    default:
                        return false;
                }
            }
        });

        mSearchProductsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearchActivity();
                    return true;
                }
                return false;
            }
        });
    }

    private void onProfileMenuItemClick() {
//        if (!isThereAccount()) {
//            startEntryActivity(REQUEST_CODE_PROFILE_ACTIVITY);
//        }

//         remove account
        Account account = getAccountOrNull();
        AccountManager accountManager = AccountManager.get(MainActivity.this);
        if (account == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            boolean isRemoved = accountManager.removeAccountExplicitly(account);
            LOGD(TAG, "account removed " + isRemoved);
        } else {
            accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
                @Override public void run(AccountManagerFuture<Boolean> future) {
                    try {
                       LOGD(TAG, "account removed " + future.getResult());
                    } catch (OperationCanceledException | IOException | AuthenticatorException e) {
                        LOGE(TAG, "Account removed error", e);
                    }
                }
            }, null);
        }
    }

    private void onNotificationMenuItemClick() {
        if (!isThereAccount()) {
            startEntryActivity(REQUEST_CODE_NOTIFICATIONS_ACTIVITY);
        }
    }

    private void onWatchingMenuItemClick() {
        if (!isThereAccount()) {
            startEntryActivity(REQUEST_CODE_WATCHING_ACTIVITY);
        }
    }

    @OnClick(R.id.sell_something_button)
    protected void onSellSomethingButtonClick() {
        if (isThereAccount()) startSellingActivity();
        else startEntryActivity(REQUEST_CODE_SELLING_ACTIVITY);
    }

    private void startEntryActivity(int requestCode) {
        startActivityForResult(EntryActivity.getStartIntent(MainActivity.this), requestCode);
    }

    private void startSellingActivity() {
        startActivity(SellingActivity.getStartIntent(MainActivity.this));
    }

    private boolean isThereAccount() {
        Account account = getAccountOrNull();
        return account != null;
    }

    @Nullable private Account getAccountOrNull() {
        AccountManager accountManager = AccountManager.get(MainActivity.this);
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.authenticator_account_type));
        if (accounts.length == 0) return null;
        else return accounts[0];
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void startSearchActivity() {
        startActivity(SearchActivity.getStartIntent(MainActivity.this));
    }

    @OnClick(R.id.browse_categories_button)
    protected void onBrowseCategoriesButtonClick() {
        displayCategoriesDialog();
    }

    private void displayCategoriesDialog() {
        CategoriesDialog dialog = CategoriesDialog.newInstance();
        dialog.show(getFragmentManager(), dialog.getClass().getCanonicalName());
    }

    @OnClick(R.id.menu_button)
    protected void onMenuButtonClick() {
        openDrawer();
    }

    private void openDrawer() {
        mDrawerLayout.openDrawer(mNavigationView);
    }

    @Override public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawer(mNavigationView);
    }
}
