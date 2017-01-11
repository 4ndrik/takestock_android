package com.devabit.takestock.screen.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.R;

/**
 * Created by Victor Artemyev on 14/06/2016.
 */
public class AboutActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AboutActivity.class);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(AboutActivity.this);
        setUpToolbar();
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(AboutActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle(R.string.profile_account_activity_about);
    }

    @OnClick(R.id.terms_text_view)
    protected void onTermsTextViewClick() {
        openTermsAndConditions();
    }

    private void openTermsAndConditions() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_terms_of_services)));
        startActivity(browserIntent);
    }
}

