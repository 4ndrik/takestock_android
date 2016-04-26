package com.devabit.takestock.ui.entry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.R;
import com.devabit.takestock.ui.signIn.SignInActivity;
import com.devabit.takestock.util.FontCache;

import java.util.List;

/**
 * Created by Victor Artemyev on 12/04/2016.
 */
public class EntryActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, EntryActivity.class);
    }

    @Bind({R.id.sign_in_button, R.id.sign_up_button})
    protected List<Button> mButtonList;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(EntryActivity.this);

        final Typeface boldTypeface = FontCache.getTypeface(EntryActivity.this, R.string.font_brandon_bold);
        ButterKnife.apply(mButtonList, new ButterKnife.Action<Button>() {
            @Override public void apply(Button view, int index) {
                view.setTypeface(boldTypeface);
            }
        });
    }

    @OnClick(R.id.sign_up_button)
    protected void onSignUpButtonClick() {
        startActivity(SignUpActivity.getStartIntent(EntryActivity.this));
    }

    @OnClick(R.id.sign_in_button)
    protected void onSignInButtonClick() {
        startActivity(SignInActivity.getStartIntent(EntryActivity.this));
    }
}
