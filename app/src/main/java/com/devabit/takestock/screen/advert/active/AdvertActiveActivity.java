package com.devabit.takestock.screen.advert.active;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Advert;
import com.devabit.takestock.data.model.Notification;
import com.devabit.takestock.data.model.Photo;
import com.devabit.takestock.screen.advert.editor.AdvertEditorActivity;
import com.devabit.takestock.screen.advert.active.fragment.offers.OffersFragment;
import com.devabit.takestock.screen.advert.active.fragment.questions.QuestionsFragment;
import com.devabit.takestock.widget.ControllableAppBarLayout;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 30/09/2016.
 */

public class AdvertActiveActivity extends AppCompatActivity implements AdvertActiveContract.View {

    private static final String EXTRA_ADVERT_ID = "EXTRA_ADVERT_ID";
    private static final String EXTRA_NOTIFICATION = "EXTRA_NOTIFICATION";

    public static Intent getStartIntent(Context context, int advertId) {
        Intent starter = new Intent(context, AdvertActiveActivity.class);
        starter.putExtra(EXTRA_ADVERT_ID, advertId);
        return starter;
    }

    public static Intent getStartIntent(Context context, Notification notification) {
        Intent starter = new Intent(context, AdvertActiveActivity.class);
        starter.putExtra(EXTRA_NOTIFICATION, notification);
        return starter;
    }

    private static final int RC_EDIT = 1001;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.advert_image_view) ImageView mImageView;
    @BindView(R.id.advert_name_text_view) TextView mNameTextView;
    @BindView(R.id.qty_available_text_view) TextView mQtyAvailableTextView;
    @BindView(R.id.days_left_text_view) TextView mDaysLeftTextView;
    @BindView(R.id.guide_price_text_view) TextView mGuidePriceTextView;
    @BindView(R.id.table_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;

    @Nullable Notification mNotification;
    Advert mAdvert;
    AdvertActiveContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_active);
        ButterKnife.bind(AdvertActiveActivity.this);
        setUpToolbar();
        createPresenter(getAdvertId());
    }

    private int getAdvertId() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_NOTIFICATION)) {
            mNotification = intent.getParcelableExtra(EXTRA_NOTIFICATION);
            return mNotification.getAdvertId();
        }
        return intent.getIntExtra(EXTRA_ADVERT_ID, 0);
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(AdvertActiveActivity.this, R.id.toolbar);
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
                        startAdvertEditorActivity();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void startAdvertEditorActivity() {
        startActivityForResult(AdvertEditorActivity.getStartIntent(AdvertActiveActivity.this, mAdvert), RC_EDIT);
    }

    private void createPresenter(int advertId) {
        new AdvertActivePresenter(advertId,
                Injection.provideDataRepository(AdvertActiveActivity.this), AdvertActiveActivity.this);
    }

    @Override public void setPresenter(@NonNull AdvertActiveContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.loadAdvert();
        if (mNotification != null && mNotification.isNew()) mPresenter.readNotification(mNotification);
    }

    @Override public void showAdvertInView(Advert advert) {
        Timber.d("Advert: %s", advert);
        mAdvert = advert;
        setUpAppBarLayout(advert);
        setUpTabLayout(advert);
        bindAdvert(advert);
    }

    private void setUpAppBarLayout(final Advert advert) {
        ControllableAppBarLayout appBarLayout = ButterKnife.findById(AdvertActiveActivity.this, R.id.appbar_layout);
        appBarLayout.setOnStateChangeListener(new ControllableAppBarLayout.OnStateChangeListener() {
            @Override public void onStateChange(int toolbarChange) {
                switch (toolbarChange) {
                    case ControllableAppBarLayout.State.COLLAPSED:
                        mToolbar.setTitle(advert.getName());
                        mToolbar.setSubtitle(getString(R.string.advert_selling_activity_guide_price, advert.getGuidePrice(), advert.getPackagingName()));
                        break;
                    case ControllableAppBarLayout.State.EXPANDED:
                    case ControllableAppBarLayout.State.IDLE: // Just fired once between switching states
                        mToolbar.setTitle("");
                        mToolbar.setSubtitle("");
                        break;
                }
            }
        });
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

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_EDIT && resultCode == RESULT_OK) {
            Advert advert = data.getParcelableExtra(getString(R.string.extra_advert));
            bindAdvert(advert);
        }
    }

    private void bindAdvert(Advert advert) {
        mAdvert = advert;
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
                    .load(photos.get(0).getImage())
                    .placeholder(R.color.grey_400)
                    .error(R.drawable.ic_image_48dp)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mImageView);
        }
    }

    @Override public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.extra_advert), mAdvert);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
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
