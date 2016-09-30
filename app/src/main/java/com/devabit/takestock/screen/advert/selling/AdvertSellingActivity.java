package com.devabit.takestock.screen.advert.selling;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.screen.advert.selling.fragment.offers.OffersFragment;
import com.devabit.takestock.screen.advert.selling.fragment.questions.QuestionsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 30/09/2016.
 */

public class AdvertSellingActivity extends AppCompatActivity {

    private static final String EXTRA_ADVERT = "EXTRA_ADVERT";

    public static Intent getStartIntent(Context context, Advert advert) {
        Intent starter = new Intent(context, AdvertSellingActivity.class);
        starter.putExtra(EXTRA_ADVERT, advert);
        return starter;
    }

    @BindView(R.id.advert_image_view) ImageView mImageView;
    @BindView(R.id.advert_name_text_view) TextView mNameTextView;
    @BindView(R.id.qty_available_text_view) TextView mQtyAvailableTextView;
    @BindView(R.id.days_left_text_view) TextView mDaysLeftTextView;
    @BindView(R.id.guide_price_text_view) TextView mGuidePriceTextView;
    @BindView(R.id.table_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_selling);
        ButterKnife.bind(AdvertSellingActivity.this);
        setUpToolbar();
        Advert advert = getIntent().getParcelableExtra(EXTRA_ADVERT);
        setUpAdvert(advert);
        setUpTabLayout(advert);
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(AdvertSellingActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.advert_selling_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit_advert:
                        Toast.makeText(AdvertSellingActivity.this, "Edit Advert not yet implemented!", Toast.LENGTH_LONG).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setUpAdvert(Advert advert) {
        mNameTextView.setText(advert.getName());
        mQtyAvailableTextView.setText(getString(R.string.advert_selling_activity_available, advert.getItemsCountNow(), advert.getPackagingName()));
        mDaysLeftTextView.setText(getString(R.string.advert_selling_activity_days_left, advert.getDaysLeft()));
        mGuidePriceTextView.setText(getString(R.string.advert_selling_activity_guide_price, advert.getGuidePrice(), advert.getPackagingName()));
        setUpPhotos(advert.getPhotos());
    }

    void setUpPhotos(List<Photo> photos) {
        if (photos.isEmpty()) {
            mImageView.setImageResource(R.drawable.ic_image_48dp);
        } else {
            Glide.with(mImageView.getContext())
                    .load(photos.get(0).getImagePath())
                    .placeholder(R.color.grey_400)
                    .error(R.drawable.ic_image_48dp)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mImageView);
        }
    }

    private void setUpTabLayout(Advert advert) {
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(
                OffersFragment.newInstance(advert),
                getResources().getQuantityString(R.plurals.offers, Integer.valueOf(advert.getOffersCount()), advert.getOffersCount()));
        pagerAdapter.addFragment(
                QuestionsFragment.newInstance(advert.getId()),
                getResources().getQuantityString(R.plurals.questions, Integer.valueOf(advert.getQuestionsCount()), advert.getQuestionsCount()));
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    static class PagerAdapter extends FragmentPagerAdapter {

        final List<Fragment> mFragmentList = new ArrayList<>();
        final List<String> mFragmentTitleList = new ArrayList<>();

        PagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}