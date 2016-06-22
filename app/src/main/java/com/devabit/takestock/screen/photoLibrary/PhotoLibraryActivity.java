package com.devabit.takestock.screen.photoLibrary;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.devabit.takestock.R;
import com.devabit.takestock.utils.FontCache;

/**
 * Created by Victor Artemyev on 12/05/2016.
 */
public class PhotoLibraryActivity extends AppCompatActivity {

    private static final String TAG = "PhotoLibraryActivity";

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_library);
        ButterKnife.bind(PhotoLibraryActivity.this);

        Toolbar toolbar = ButterKnife.findById(PhotoLibraryActivity.this, R.id.toolbar);
        toolbar.inflateMenu(R.menu.profile_account_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView titleToolbar = ButterKnife.findById(toolbar, R.id.toolbar_title);
        final Typeface boldTypeface = FontCache.getTypeface(PhotoLibraryActivity.this, R.string.font_brandon_bold);
        titleToolbar.setTypeface(boldTypeface);
        titleToolbar.setText("Select Photo");

        RecyclerView recyclerView = ButterKnife.findById(PhotoLibraryActivity.this, R.id.recycler_view);

    }
}
