package com.devabit.takestock.ui.main;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
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

public class MainActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
//        starter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return starter;
    }

    @BindView(R.id.drawer_layout) protected DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) protected NavigationView mNavigationView;
    @BindView(R.id.search_products_edit_text) protected EditText mSearchProductsEditText;
    @BindView(R.id.browse_categories_button) protected Button mBrowseProductsButton;
    @BindView(R.id.sell_something_button) protected Button mSellSomethingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        if (shouldBeEntryActivityDisplayed()) {
            startActivity(EntryActivity.getStartIntent(MainActivity.this));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        ButterKnife.bind(MainActivity.this);
        Typeface mediumTypeface = FontCache.getTypeface(MainActivity.this, R.string.font_brandon_medium);
        mSearchProductsEditText.setTypeface(mediumTypeface);

        Typeface boldTypeface = FontCache.getTypeface(MainActivity.this, R.string.font_brandon_bold);
        mBrowseProductsButton.setTypeface(boldTypeface);
        mSellSomethingButton.setTypeface(boldTypeface);

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

    private void startSearchActivity() {
        startActivity(SearchActivity.getStartIntent(MainActivity.this));
    }

    private boolean shouldBeEntryActivityDisplayed() {
        AccountManager accountManager = AccountManager.get(MainActivity.this);
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.authenticator_account_type));
        return accounts.length == 0;
    }

    @OnClick(R.id.sell_something_button)
    protected void startSellingActivity() {
        startActivity(new Intent(this, SellingActivity.class));
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
