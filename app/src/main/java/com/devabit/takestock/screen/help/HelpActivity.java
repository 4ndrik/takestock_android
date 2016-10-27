package com.devabit.takestock.screen.help;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.devabit.takestock.R;

/**
 * Created by Victor Artemyev on 14/06/2016.
 */
public class HelpActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, HelpActivity.class);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setUpToolbar();
        setUpGuideTextView();
        setUpQuestionTextView();
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(HelpActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle(R.string.profile_account_activity_help_and_contact);
    }

    private void setUpGuideTextView() {
        TextView textView = ButterKnife.findById(HelpActivity.this, R.id.guide_text_view);
        String guideHelp = getString(R.string.help_guide);
        String website = getString(R.string.website);
        SpannableString text = new SpannableString(guideHelp + " " + website);
        text.setSpan(new ForegroundColorSpan(getColorFromRes(R.color.grey_600)), 0, guideHelp.length(), 0);
        text.setSpan(mClickableSpan, text.length() - website.length(), text.length(), 0);
        textView.setText(text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setUpQuestionTextView() {
        TextView textView = ButterKnife.findById(HelpActivity.this, R.id.question_text_view);
        String questionHelp = getString(R.string.help_questions_message);
        String email = getString(R.string.admin_email);
        SpannableString text = new SpannableString(questionHelp + " " + email);
        text.setSpan(new ForegroundColorSpan(getColorFromRes(R.color.grey_600)), 0, questionHelp.length(), 0);
        text.setSpan(mClickableSpan, text.length() - email.length(), text.length(), 0);
        textView.setText(text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private int getColorFromRes(@ColorRes int colorRes) {
        return ContextCompat.getColor(HelpActivity.this, colorRes);
    }

    private final ClickableSpan mClickableSpan
            = new ClickableSpan() {
        @Override public void onClick(View widget) {
            switch (widget.getId()) {
                case R.id.guide_text_view:
                    openWebsite();
                    break;

                case R.id.question_text_view:
                    sendQuestion();
                    break;

            }
        }
    };

    private void openWebsite() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.website_url)));
        startActivity(browserIntent);
    }

    private void sendQuestion() {
        Intent intent = new Intent((Intent.ACTION_SEND));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {getString(R.string.admin_email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Question");
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(R.string.send_question)));
    }
}
