package com.devabit.takestock.screens.entry;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.screens.entry.fragments.signIn.SignInFragment;
import com.devabit.takestock.screens.entry.fragments.signIn.SignInPresenter;

/**
 * Created by Victor Artemyev on 12/04/2016.
 */
public class EntryActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, EntryActivity.class);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        SignInFragment singInFragment = SignInFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.content_activity_entry, singInFragment)
                .commit();

        new SignInPresenter(
                Injection.provideDataRepository(EntryActivity.this), singInFragment);
    }

    @Override public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
