package com.devabit.takestock.screen.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.screen.accountProfile.ProfileAccountActivity;
import com.devabit.takestock.screen.advert.editor.AdvertEditorActivity;
import com.devabit.takestock.screen.adverts.AdvertsActivity;
import com.devabit.takestock.screen.buying.BuyingActivity;
import com.devabit.takestock.screen.category.CategoriesActivity;
import com.devabit.takestock.screen.dialog.accountNotVerified.AccountNotVerifiedDialog;
import com.devabit.takestock.screen.dialog.emailConfirmation.EmailConfirmationDialog;
import com.devabit.takestock.screen.entry.EntryActivity;
import com.devabit.takestock.screen.selling.SellingActivity;
import com.devabit.takestock.screen.watching.WatchingActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    public static Intent getStartIntent(Context context, String action) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        starter.setAction(action);
        return starter;
    }

    private static final int RC_ENTRY = 100;

    private static final int INDEX_MAIN_CONTENT = 0;
    private static final int INDEX_NO_CONNECTION_CONTENT = 1;

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.content_activity_main) View mContent;
    @BindView(R.id.view_switcher) ViewSwitcher mViewSwitcher;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.search_products_edit_text) EditText mSearchEditText;
    @BindView(R.id.browse_categories_button) Button mBrowseProductsButton;
    @BindView(R.id.sell_something_button) Button mSellSomethingButton;

    @BindViews({R.id.menu_button, R.id.logo_image_view, R.id.search_products_edit_text,
            R.id.browse_categories_button, R.id.sell_something_button})
    protected List<View> mViews;

    private TakeStockAccount mAccount;
    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        mAccount = TakeStockAccount.get(MainActivity.this);
        createPresenter();
        setUpSearchEditText();
        setUpNavigationItemSelectedListener();
    }

    private void createPresenter() {
        new MainPresenter(Injection.provideDataRepository(MainActivity.this), MainActivity.this);
    }

    @Override public void setPresenter(@NonNull MainContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.updateData(mAccount.getId());
    }

    private void setUpSearchEditText() {
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = mSearchEditText.getText().toString().trim();
                    if (TextUtils.isEmpty(query)) return false;
                    startAdvertsActivity(query);
                    return true;
                }
                return false;
            }
        });

        mSearchEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableWidth = mSearchEditText.getCompoundDrawables()[DRAWABLE_END].getBounds().width();
                    float drawableCoordinate = mSearchEditText.getRight() - drawableWidth;
                    if (event.getRawX() >= drawableCoordinate) {
                        String query = getSearchQuery();
                        if (TextUtils.isEmpty(query)) return false;
                        startAdvertsActivity(query);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private String getSearchQuery() {
        return mSearchEditText.getText().toString().trim();
    }

    private void startAdvertsActivity(String query) {
        startActivity(AdvertsActivity.getSearchingStartIntent(MainActivity.this, query));
    }

    private void setUpNavigationItemSelectedListener() {

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.nav_profile:
                        onProfileMenuItemClick();
                        return true;

                    case R.id.nav_notifications:
                        onNotificationMenuItemClick();
                        return true;

                    case R.id.nav_selling:
                        onSellingMenuItemClick();
                        return true;

                    case R.id.nav_buying:
                        onBuyingMenuItemClick();
                        return true;

                    case R.id.nav_watching:
                        onWatchingMenuItemClick();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void onProfileMenuItemClick() {
        if (!mAccount.lacksAccount()) {
            startProfileAccountActivity();
        } else {
            startEntryActivity();
        }
    }

    private void startProfileAccountActivity() {
        startActivity(ProfileAccountActivity.getStartIntent(MainActivity.this));
    }

    private void onNotificationMenuItemClick() {
        showNotYetImplementedSnackbar();
    }

    private void showNotYetImplementedSnackbar() {
        Snackbar.make(mContent, "Not yet implemented.", Snackbar.LENGTH_LONG).show();
    }

    private void onSellingMenuItemClick() {
        if (isAccountValid()) {
            startSellingActivity();
        }
    }

    private void startSellingActivity() {
        startActivity(SellingActivity.getStartIntent(MainActivity.this));
    }

    private void onBuyingMenuItemClick() {
        if (isAccountValid()) {
            startBuyingActivity();
        }
    }

    private void startBuyingActivity() {
        startActivity(BuyingActivity.getStartIntent(MainActivity.this));
    }

    private void onWatchingMenuItemClick() {
        if (isAccountValid()) {
            startWatchingActivity();
        }
    }

    private void startWatchingActivity() {
        startActivity(WatchingActivity.getStartIntent(MainActivity.this));
    }

    @OnClick(R.id.sell_something_button)
    protected void onSellSomethingButtonClick() {
        if (isAccountValid()) {
            startAdvertEditorActivity();
        }
    }

    private void startAdvertEditorActivity() {
        startActivity(AdvertEditorActivity.getStartIntent(MainActivity.this));
    }

    private boolean isAccountValid() {
        if (mAccount.lacksAccount()) {
            startEntryActivity();
            return false;
        }
        if (!mAccount.isVerified()) {
            displayEmailConfirmationDialog();
            return false;
        }

        if (!mAccount.isVerifiedByStaff()) {
            displayAccountNotActivatedDialog();
            return false;
        }

        return true;
    }

    private void startEntryActivity() {
        startActivityForResult(EntryActivity.getStartIntent(MainActivity.this), RC_ENTRY);
    }

    private void displayEmailConfirmationDialog() {
        EmailConfirmationDialog dialog = EmailConfirmationDialog.newInstance(mAccount.getEmail());
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    private void displayAccountNotActivatedDialog() {
        AccountNotVerifiedDialog dialog = AccountNotVerifiedDialog.newInstance();
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ENTRY && resultCode == RESULT_OK) {
            setUpHeaderNavigationView();
        }
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null || TextUtils.isEmpty(intent.getAction())) return;
        String action = intent.getAction();
        if (action.equals(getString(R.string.action_sign_in))
                || action.equals(getString(R.string.action_sign_up))
                || action.equals(getString(R.string.action_log_out))) {
            setUpHeaderNavigationView();
        }
    }

//    private boolean lacksAccount() {
//        return mAccount.lacksAccount();
//    }

    @OnClick(R.id.browse_categories_button)
    protected void onBrowseCategoriesButtonClick() {
        startActivity(CategoriesActivity.getStartIntent(MainActivity.this));
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
        setUpHeaderNavigationView();
    }

    private void setUpHeaderNavigationView() {
        TextView titleTextView = ButterKnife.findById(mNavigationView.getHeaderView(0), R.id.title_drawer_header_text_view);
        if (mAccount.lacksAccount()) {
            titleTextView.setText(R.string.sign_in);
            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    closeDrawer();
                    startEntryActivity();
                }
            });
        } else {
            titleTextView.setText(mAccount.getName());
            titleTextView.setOnClickListener(null);
        }
    }

    @Override public void showLoadingDataError() {
        startEntryActivity();
    }

    @Override public void showNetworkConnectionError() {
        mViewSwitcher.setDisplayedChild(INDEX_NO_CONNECTION_CONTENT);
        showNoConnectionSnackbar();
    }

    private void showNoConnectionSnackbar() {
        Snackbar.make(mContent, R.string.no_connection, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        mPresenter.updateData(mAccount.getId());
                    }
                })
                .setActionTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary))
                .show();
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


    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
