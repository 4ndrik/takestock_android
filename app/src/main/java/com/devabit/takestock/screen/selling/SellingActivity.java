package com.devabit.takestock.screen.selling;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.R;
import com.devabit.takestock.screen.selling.fragment.BaseAdvertsFragment;
import com.devabit.takestock.screen.selling.fragment.activeAdverts.ActiveAdvertsFragment;
import com.devabit.takestock.screen.selling.fragment.draftAdverts.DraftAdvertsAdvertsFragment;
import com.devabit.takestock.screen.selling.fragment.expiredAdverts.ExpiredAdvertsAdvertsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 08/10/2016.
 */

public class SellingActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SellingActivity.class);
    }

    static final int POS_ACTIVE = 0;
    static final int POS_DRAFT = 1;
    static final int POS_EXPIRED = 2;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;

    PagerAdapter mPagerAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_selling);
        ButterKnife.bind(SellingActivity.this);
        setUpToolbar();
        setUpTabLayout();
    }

    private void setUpToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpTabLayout() {
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(ActiveAdvertsFragment.newInstance());
        mPagerAdapter.addFragment(DraftAdvertsAdvertsFragment.newInstance());
        mPagerAdapter.addFragment(ExpiredAdvertsAdvertsFragment.newInstance());
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {

                int currentItem = mViewPager.getCurrentItem();
                BaseAdvertsFragment fragment = (BaseAdvertsFragment) mPagerAdapter.getItem(currentItem);
                fragment.onHide();

                int position = tab.getPosition();
                switch (tab.getPosition()) {
                    case POS_ACTIVE:
                        mViewPager.setCurrentItem(position);
                        mToolbar.setTitle(R.string.selling_toolbar_title_active);
                        break;
                    case POS_DRAFT:
                        mViewPager.setCurrentItem(position);
                        mToolbar.setTitle(R.string.selling_toolbar_title_draft);
                        break;
                    case POS_EXPIRED:
                        mViewPager.setCurrentItem(position);
                        mToolbar.setTitle(R.string.selling_toolbar_title_expired);
                        break;
                }

                currentItem = mViewPager.getCurrentItem();
                fragment = (BaseAdvertsFragment) mPagerAdapter.getItem(currentItem);
                fragment.onDisplay();
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.selector_active_adverts), true);
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.selector_draft_adverts));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.selector_expired_adverts));
    }

    static class PagerAdapter extends FragmentPagerAdapter {

        final List<Fragment> mFragments = new ArrayList<>();

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override public int getCount() {
            return mFragments.size();
        }

        void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }
    }
}
