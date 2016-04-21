package com.devabit.takestock.entry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.devabit.takestock.R;

/**
 * Created by Victor Artemyev on 15/04/2016.
 */
public class SignInActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SignInActivity.class);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }
}
