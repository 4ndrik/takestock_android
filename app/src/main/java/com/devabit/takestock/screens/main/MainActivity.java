package com.devabit.takestock.screens.main;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.screens.buying.BuyingActivity;
import com.devabit.takestock.screens.category.CategoriesDialog;
import com.devabit.takestock.screens.entry.EntryActivity;
import com.devabit.takestock.screens.profile.account.ProfileAccountActivity;
import com.devabit.takestock.screens.search.SearchActivity;
import com.devabit.takestock.screens.sellSomething.SellSomethingActivity;
import com.devabit.takestock.screens.selling.SellingActivity;
import com.devabit.takestock.util.FontCache;

import java.util.List;

import static com.devabit.takestock.util.Logger.makeLogTag;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private static final String TAG = makeLogTag(MainActivity.class);

    public static Intent getStartIntent(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
//        starter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return starter;
    }

    private static final int REQUEST_CODE_MAIN_ACTIVITY = 100;
    private static final int REQUEST_CODE_SELLING_ACTIVITY = 101;
    private static final int REQUEST_CODE_PROFILE_ACTIVITY = 102;
    private static final int REQUEST_CODE_NOTIFICATIONS_ACTIVITY = 103;
    private static final int REQUEST_CODE_WATCHING_ACTIVITY = 104;
    private static final int REQUEST_CODE_OFFERS_ACTIVITY = 105;

    private static final int INDEX_MAIN_CONTENT = 0;
    private static final int INDEX_NO_CONNECTION_CONTENT = 1;

    @BindView(R.id.drawer_layout) protected DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) protected NavigationView mNavigationView;
    @BindView(R.id.content_activity_main) protected View mContent;
    @BindView(R.id.view_switcher) protected ViewSwitcher mViewSwitcher;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.search_products_edit_text) protected EditText mSearchProductsEditText;
    @BindView(R.id.browse_categories_button) protected Button mBrowseProductsButton;
    @BindView(R.id.sell_something_button) protected Button mSellSomethingButton;

    @BindViews({R.id.menu_button, R.id.logo_image_view, R.id.search_products_edit_text,
            R.id.browse_categories_button, R.id.sell_something_button})
    protected List<View> mViews;

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        new MainPresenter(
                Injection.provideDataRepository(MainActivity.this), MainActivity.this);

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

                    case R.id.nav_buying:
                        onBuyingMenuItemClick();
                        return true;

                    case R.id.nav_selling:
                        onSellingMenuItemClick();
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

        setUpTitleNavigationView();
    }

    private void onProfileMenuItemClick() {
        if (lacksAccount()) startEntryActivity(REQUEST_CODE_PROFILE_ACTIVITY);
        else startProfileAccountActivity();
    }

    private void startProfileAccountActivity() {
        startActivityForResult(ProfileAccountActivity.getStartIntent(MainActivity.this), REQUEST_CODE_PROFILE_ACTIVITY);
    }

    private void onNotificationMenuItemClick() {
//        if (lacksAccount()) startEntryActivity(REQUEST_CODE_NOTIFICATIONS_ACTIVITY);
        showNotYetImplementedSnackbar();

    }

    private void onWatchingMenuItemClick() {
//        if (lacksAccount()) startEntryActivity(REQUEST_CODE_WATCHING_ACTIVITY);
        showNotYetImplementedSnackbar();
    }

    private void onBuyingMenuItemClick() {
        closeDrawer();
        if (lacksAccount()) startEntryActivity(REQUEST_CODE_OFFERS_ACTIVITY);
        else startBuyingActivity();
    }

    private void onSellingMenuItemClick() {
        closeDrawer();
        if (lacksAccount()) startEntryActivity(REQUEST_CODE_SELLING_ACTIVITY);
        else startSellingActivity();
    }

    private void startBuyingActivity() {
        startActivity(BuyingActivity.getStartIntent(MainActivity.this));
    }

    @OnClick(R.id.sell_something_button)
    protected void onSellSomethingButtonClick() {
        closeDrawer();
        if (lacksAccount()) startEntryActivity(REQUEST_CODE_SELLING_ACTIVITY);
        else startSellSomethingActivity();
    }

    private void startEntryActivity(int requestCode) {
        startActivityForResult(EntryActivity.getStartIntent(MainActivity.this), requestCode);
    }

    private void startSellingActivity() {
        startActivity(SellingActivity.getStartIntent(MainActivity.this));
    }

    private void startSellSomethingActivity() {
        startActivity(SellSomethingActivity.getStartIntent(MainActivity.this));
    }

    private boolean lacksAccount() {
        Account account = getAccountOrNull();
        return account == null;
    }

    @Nullable private Account getAccountOrNull() {
        AccountManager accountManager = AccountManager.get(MainActivity.this);
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.authenticator_account_type));
        if (accounts.length == 0) return null;
        else return accounts[0];
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setUpTitleNavigationView();
        }
    }

    private void setUpTitleNavigationView() {
        TextView titleTextView = ButterKnife.findById(mNavigationView.getHeaderView(0), R.id.title_drawer_header_text_view);
        Account account = getAccountOrNull();
        if (account == null) {
            titleTextView.setText(R.string.sign_up);
            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    startEntryActivity(REQUEST_CODE_MAIN_ACTIVITY);
                    closeDrawer();
                }
            });
        } else {
            titleTextView.setText(account.name);
            titleTextView.setOnClickListener(null);
        }
    }

    private void startSearchActivity() {
        startActivity(SearchActivity.getStartIntent(MainActivity.this));
    }

    @Override protected void onStart() {
        super.onStart();
        mPresenter.resume();
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

    @Override public void showDataUpdated() {
        mViewSwitcher.setDisplayedChild(INDEX_MAIN_CONTENT);
    }

    @Override public void showLoadingDataError() {
        startEntryActivity(REQUEST_CODE_MAIN_ACTIVITY);
    }

    @Override public void showNetworkConnectionError() {
        mViewSwitcher.setDisplayedChild(INDEX_NO_CONNECTION_CONTENT);
        showNoConnectionSnackbar();
    }

    private void showNoConnectionSnackbar() {
        Snackbar.make(mContent, R.string.no_connection, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        mPresenter.updateData();
                    }
                })
                .setActionTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary))
                .show();
    }

    private void showNotYetImplementedSnackbar() {
        Snackbar.make(mContent, "Not yet implemented.", Snackbar.LENGTH_LONG).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        mProgressBar.setVisibility(isActive ? View.VISIBLE : View.GONE);
        setTouchDisabled(isActive);
        ButterKnife.apply(mViews, TRANSPARENCY, isActive);
    }

    private void setTouchDisabled(boolean isActive) {
        Window window = getWindow();
        if (isActive) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private static final ButterKnife.Setter<View, Boolean> TRANSPARENCY
            = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(@NonNull View view, Boolean isActive, int index) {
            view.setAlpha(isActive ? 0.5f : 1.0f);
        }
    };

    @Override public void setPresenter(@NonNull MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }
}
