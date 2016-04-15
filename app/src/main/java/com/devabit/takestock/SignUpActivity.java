package com.devabit.takestock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.devabit.takestock.util.FontCache;

/**
 * Created by Victor Artemyev on 12/04/2016.
 */
public class SignUpActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    @Bind(R.id.toolbar) protected Toolbar mToolbar;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(SignUpActivity.this);
        Typeface boldTypeface = FontCache.getTypeface(SignUpActivity.this, R.string.font_brandon_bold);
        Typeface mediumTypeface = FontCache.getTypeface(SignUpActivity.this, R.string.font_brandon_medium);
//        setUpToolbar(boldTypeface, mediumTypeface);
    }

//    private void setUpToolbar(Typeface boldTypeface, Typeface mediumTypeface) {
//        TextView title = (TextView) mToolbar.findViewById(R.id.toolbar_title);
//        title.setTypeface(boldTypeface);
//        title.setText(R.string.sign_up);
//
//        TextView back = (TextView) mToolbar.findViewById(R.id.toolbar_back);
//        back.setTypeface(mediumTypeface);
//        back.setText(R.string.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//    }
}
