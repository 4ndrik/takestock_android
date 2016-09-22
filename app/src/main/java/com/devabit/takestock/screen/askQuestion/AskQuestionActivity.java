package com.devabit.takestock.screen.askQuestion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.model.Question;
import com.devabit.takestock.screen.askQuestion.adapters.QuestionsAdapter;
import com.devabit.takestock.widget.DividerItemDecoration;

import java.util.List;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class AskQuestionActivity extends AppCompatActivity implements AskQuestionContract.View {

    private static final String EXTRA_ADVERT_ID = "ADVERT_ID";

    public static Intent getStartIntent(Context context, int advertId) {
        Intent starter = new Intent(context, AskQuestionActivity.class);
        starter.putExtra(EXTRA_ADVERT_ID, advertId);
        return starter;
    }

    @BindView(R.id.content_activity_questions) protected View mContent;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_view) protected RecyclerView mRecyclerView;
    @BindView(R.id.question_edit_text) protected EditText mQuestionEditText;
    @BindView(R.id.send_question_button) protected ImageButton mSendQuestionButton;

    private int mAdvertId;
    private TakeStockAccount mAccount;
    private QuestionsAdapter mQuestionsAdapter;
    private AskQuestionContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        ButterKnife.bind(AskQuestionActivity.this);
        mAdvertId = getIntent().getIntExtra(EXTRA_ADVERT_ID, 0);
        mAccount = TakeStockAccount.get(AskQuestionActivity.this);
        setUpToolbar();
        setUpRefreshLayout();
        setUpRecyclerView();
        createPresenter();
    }

    private void createPresenter() {
        new AskQuestionPresenter(
                Injection.provideDataRepository(AskQuestionActivity.this), AskQuestionActivity.this);
    }

    @Override public void setPresenter(@NonNull AskQuestionContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.fetchQuestionsWithAdvertId(mAdvertId);
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(AskQuestionActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                refreshQuestions();
            }
        });
    }

    private void refreshQuestions() {
        mPresenter.fetchQuestionsWithAdvertId(mAdvertId);
    }

    private void setUpRecyclerView() {
        mRecyclerView = ButterKnife.findById(AskQuestionActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AskQuestionActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(ContextCompat.getDrawable(AskQuestionActivity.this, R.drawable.divider_grey300));
        mRecyclerView.addItemDecoration(itemDecoration);
        mQuestionsAdapter = new QuestionsAdapter(AskQuestionActivity.this);
        mRecyclerView.setAdapter(mQuestionsAdapter);
    }

    @OnTextChanged(R.id.question_edit_text)
    protected void onQuestionTextChanged(CharSequence text) {
        setSentQuestionButtonVisibility(text.length() > 0);
    }

    private void setSentQuestionButtonVisibility(boolean isVisible) {
        mSendQuestionButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override public void showQuestionsInView(List<Question> questions) {
        mQuestionsAdapter.refreshQuestions(questions);
    }

    @Override public void showQuestionInView(Question question) {
        mQuestionsAdapter.addQuestion(question);
        mRecyclerView.smoothScrollToPosition(mQuestionsAdapter.getItemCount() - 1);
        mQuestionEditText.getText().clear();
    }

    @OnClick(R.id.send_question_button)
    protected void onQuestionSendButtonClick() {
        mPresenter.makeQuestion(createQuestion());
    }

    private Question createQuestion() {
        return new Question.Builder()
                .setUserId(mAccount.getUserId())
                .setAdvertId(mAdvertId)
                .setMessage(getMessage())
                .create();
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

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }
}
