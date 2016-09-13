package com.devabit.takestock.screen.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Category;

/**
 * Created by Victor Artemyev on 12/09/2016.
 */
public class CategoriesActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, CategoriesActivity.class);
    }

    @BindView(R.id.toolbar) protected Toolbar mToolbar;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(CategoriesActivity.this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_layout, CategoriesFragment.newInstance())
                .commit();
    }

    public void setSelectedCategory(Category category) {
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.fragment_container_layout, SubcategoriesFragment.newInstance(category))
                .addToBackStack(SubcategoriesFragment.class.getName())
                .commit();
    }

    @Override public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            mToolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        }
        super.onBackPressed();
    }
}
