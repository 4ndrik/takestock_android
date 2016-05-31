package com.devabit.takestock.screens.questions;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Question;
import com.devabit.takestock.screens.questions.adapters.QuestionsAdapter;
import com.devabit.takestock.util.FontCache;

import java.util.List;

import static com.devabit.takestock.util.Logger.LOGD;
import static com.devabit.takestock.util.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class QuestionsActivity extends AppCompatActivity implements QuestionsContract.View {

    private static final String TAG = makeLogTag(QuestionsActivity.class);

    private static final String EXTRA_ADVERT_ID = "ADVERT_ID";

    public static Intent getStartIntent(Context context, int advertId) {
        Intent starter = new Intent(context, QuestionsActivity.class);
        starter.putExtra(EXTRA_ADVERT_ID, advertId);
        return starter;
    }

    @BindView(R.id.content_activity_question) protected View mContent;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.question_edit_text) protected EditText mQuestionEditText;
    @BindView(R.id.send_question_button) protected ImageButton mSendQuestionButton;

    private int mAdvertId;
    private QuestionsAdapter mQuestionsAdapter;

    private QuestionsContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(QuestionsActivity.this);
        mAdvertId = getIntent().getIntExtra(EXTRA_ADVERT_ID, 0);
        initPresenter();
        setUpToolbar();
        setUpRefreshLayout();
        setUpQuestionRecyclerView();
    }

    private void initPresenter() {
        new QuestionsPresenter(
                Injection.provideDataRepository(QuestionsActivity.this), QuestionsActivity.this);
    }

    private void setUpToolbar() {
        final Typeface boldTypeface = FontCache.getTypeface(this, R.string.font_brandon_bold);
        Toolbar toolbar = ButterKnife.findById(QuestionsActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView titleToolbar = ButterKnife.findById(toolbar, R.id.toolbar_title);
        titleToolbar.setTypeface(boldTypeface);
        titleToolbar.setText(R.string.questions);
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                fetchQuestions();
            }
        });
    }

    private void setUpQuestionRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(QuestionsActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                QuestionsActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mQuestionsAdapter = new QuestionsAdapter(QuestionsActivity.this);
        recyclerView.setAdapter(mQuestionsAdapter);
    }

    @Override protected void onStart() {
        super.onStart();
        fetchQuestions();
    }

    private void fetchQuestions() {
        mPresenter.fetchQuestionsByAdvertId(mAdvertId);
    }

    @OnTextChanged(R.id.question_edit_text)
    protected void onQuestionTextChanged(CharSequence text) {
        LOGD(TAG, text.toString());
        setSenQuestionButtonVisibility(text.length() > 0);
    }

    private void setSenQuestionButtonVisibility(boolean isVisible) {
        mSendQuestionButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override public void showQuestionsInView(List<Question> questions) {
        mQuestionsAdapter.clearQuestions();
        mQuestionsAdapter.addQuestions(questions);
    }

    @Override public void showQuestionInView(Question question) {
        mQuestionsAdapter.addQuestion(question);
    }

    @OnClick(R.id.send_question_button)
    protected void onQuestionSendButtonClick() {
        mPresenter.makeQuestion(getQuestion());
    }

    private Question getQuestion() {
        Question question = new Question();
        question.setUserId(getUserId());
        question.setAdvertId(mAdvertId);
        question.setMessage(getMessage());
        return question;
    }

    private int getUserId() {
        AccountManager accountManager = AccountManager.get(QuestionsActivity.this);
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.authenticator_account_type));
        String userId = accountManager.getUserData(accounts[0], getString(R.string.authenticator_user_id));
        return Integer.valueOf(userId);
    }

    private String getMessage() {
        return mQuestionEditText.getText().toString().trim();
    }

    @Override public void showNetworkConnectionError() {
        showSnack(R.string.error_no_network_connection);
    }

    @Override public void showUnknownError() {
        showSnack(R.string.error_unknown);
    }

    private void showSnack(@StringRes int resId) {
        Snackbar.make(mContent, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override public void setPresenter(@NonNull QuestionsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }
}
