package com.devabit.takestock.ui.search;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import com.devabit.takestock.R;
import com.devabit.takestock.util.FontCache;

import java.util.List;

/**
 * Created by Victor Artemyev on 11/04/2016.
 */
public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @BindView(R.id.recycler_view) protected RecyclerView mRecyclerView;

    @BindViews({R.id.browse_categories_button, R.id.filter_button, R.id.newest_first_button})
    protected List<Button> mButtons;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(SearchActivity.this);

        final Typeface boldTypeface = FontCache.getTypeface(this, R.string.font_brandon_bold);
        final Typeface mediumTypeface = FontCache.getTypeface(this, R.string.font_brandon_medium);

        TextView titleToolbar = ButterKnife.findById(mToolbar, R.id.toolbar_title);
        titleToolbar.setTypeface(boldTypeface);
        titleToolbar.setText(R.string.search);
        mToolbar.inflateMenu(R.menu.main);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        ButterKnife.apply(mButtons, new ButterKnife.Action<Button>() {
            @Override public void apply(Button view, int index) {
                view.setTypeface(mediumTypeface);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new TestAdapter(this));
    }
}
