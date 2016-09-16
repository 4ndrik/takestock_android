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
import com.devabit.takestock.screen.advert.create.AdvertCreateActivity;
import com.devabit.takestock.screen.adverts.AdvertsActivity;
import com.devabit.takestock.screen.buying.BuyingActivity;
import com.devabit.takestock.screen.category.CategoriesActivity;
import com.devabit.takestock.screen.entry.EntryActivity;
import com.devabit.takestock.screen.profile.account.ProfileAccountActivity;
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

    @BindView(R.id.drawer_layout) protected DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) protected NavigationView mNavigationView;
    @BindView(R.id.content_activity_main) protected View mContent;
    @BindView(R.id.view_switcher) protected ViewSwitcher mViewSwitcher;
    @BindView(R.id.progress_bar) protected ProgressBar mProgressBar;
    @BindView(R.id.search_products_edit_text) protected EditText mSearchEditText;
    @BindView(R.id.browse_categories_button) protected Button mBrowseProductsButton;
    @BindView(R.id.sell_something_button) protected Button mSellSomethingButton;

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
//        Typeface mediumTypeface = FontCache.getTypeface(MainActivity.this, R.string.font_brandon_medium);
//        mSearchEditText.setTypeface(mediumTypeface);
//
//        Typeface boldTypeface = FontCache.getTypeface(MainActivity.this, R.string.font_brandon_bold);
//        mBrowseProductsButton.setTypeface(boldTypeface);
//        mSellSomethingButton.setTypeface(boldTypeface);

        setUpNavigationView();
        setUpSearchEditText();
        setUpTitleNavigationView();
    }

    private void createPresenter() {
        new MainPresenter(
                Injection.provideDataRepository(MainActivity.this), MainActivity.this);
    }

    @Override public void setPresenter(@NonNull MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void setUpNavigationView() {
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

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (mSearchEditText.getRight() - mSearchEditText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                        String query = mSearchEditText.getText().toString().trim();
                        if (TextUtils.isEmpty(query)) return false;
                        startAdvertsActivity(query);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void onProfileMenuItemClick() {
        closeDrawer();
        if (lacksAccount()) startEntryActivity();
        else startProfileAccountActivity();
    }

    private void startProfileAccountActivity() {
        startActivity(ProfileAccountActivity.getStartIntent(MainActivity.this));
    }

    private void onNotificationMenuItemClick() {
//        if (lacksAccount()) startEntryActivity(REQUEST_CODE_NOTIFICATIONS_ACTIVITY);
        showNotYetImplementedSnackbar();
    }

    private void onWatchingMenuItemClick() {
        closeDrawer();
        if (lacksAccount()) startEntryActivity();
        else startWatchingActivity();
    }

    private void startWatchingActivity() {
        startActivity(WatchingActivity.getStartIntent(MainActivity.this));
    }

    private void onBuyingMenuItemClick() {
        closeDrawer();
        if (lacksAccount()) startEntryActivity();
        else startBuyingActivity();
    }

    private void onSellingMenuItemClick() {
        closeDrawer();
        if (lacksAccount()) startEntryActivity();
        else startSellingActivity();
    }

    private void startBuyingActivity() {
        startActivity(BuyingActivity.getStartIntent(MainActivity.this));
    }

    @OnClick(R.id.sell_something_button)
    protected void onSellSomethingButtonClick() {
        closeDrawer();
        if (lacksAccount()) startEntryActivity();
        else startSellSomethingActivity();
    }

    private void startEntryActivity() {
//        startActivity(EntryActivity.getStartIntent(MainActivity.this));
        startActivityForResult(EntryActivity.getStartIntent(MainActivity.this), RC_ENTRY);
    }

    private void startSellingActivity() {
        startActivity(SellingActivity.getStartIntent(MainActivity.this));
    }

    private void startSellSomethingActivity() {
        startActivity(AdvertCreateActivity.getStartIntent(MainActivity.this));
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ENTRY && resultCode == RESULT_OK) {
            setUpTitleNavigationView();
        }
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null || TextUtils.isEmpty(intent.getAction())) return;
        String action = intent.getAction();
        if (action.equals(getString(R.string.action_sign_in))
                || action.equals(getString(R.string.action_sign_up))
                || action.equals(getString(R.string.action_log_out))) {
            setUpTitleNavigationView();
        }
    }

    private void setUpTitleNavigationView() {
        TextView titleTextView = ButterKnife.findById(mNavigationView.getHeaderView(0), R.id.title_drawer_header_text_view);
        if (lacksAccount()) {
            titleTextView.setText(R.string.sign_in);
            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    closeDrawer();
                    startEntryActivity();
                }
            });
        } else {
            titleTextView.setText(mAccount.getUserName());
            titleTextView.setOnClickListener(null);
        }
    }

    private boolean lacksAccount() {
        return mAccount.lacksAccount();
    }

    private void startAdvertsActivity(String qyery) {
        startActivity(AdvertsActivity.getSearchingStartIntent(MainActivity.this, qyery));
    }

    @Override protected void onStart() {
        super.onStart();
        mPresenter.resume();
    }

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

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }
}
