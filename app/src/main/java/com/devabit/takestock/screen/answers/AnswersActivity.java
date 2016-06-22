package com.devabit.takestock.screen.answers;

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
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Answer;
import com.devabit.takestock.data.models.Question;
import com.devabit.takestock.screen.answers.adapters.AnswersAdapter;
import com.devabit.takestock.screen.answers.dialog.AnswerDialog;
import com.devabit.takestock.utils.FontCache;

import java.util.List;

import static com.devabit.takestock.utils.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
public class AnswersActivity extends AppCompatActivity implements AnswersContract.View {

    private static final String TAG = makeLogTag(AnswersActivity.class);

    private static final String EXTRA_ADVERT_ID = "ADVERT_ID";

    public static Intent getStartIntent(Context context, int advertId) {
        Intent starter = new Intent(context, AnswersActivity.class);
        starter.putExtra(EXTRA_ADVERT_ID, advertId);
        return starter;
    }

    @BindView(R.id.content_activity_questions) protected View mContent;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;

    private int mAdvertId;
    private AnswersAdapter mAnswersAdapter;

    private AnswersContract.Presenter mPresenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        ButterKnife.bind(AnswersActivity.this);
        mAdvertId = getIntent().getIntExtra(EXTRA_ADVERT_ID, 0);
        initPresenter();
        setUpToolbar();
        setUpRefreshLayout();
        setUpQuestionRecyclerView();
    }

    private void initPresenter() {
        new AnswersPresenter(
                Injection.provideDataRepository(AnswersActivity.this), AnswersActivity.this);
    }

    private void setUpToolbar() {
        final Typeface boldTypeface = FontCache.getTypeface(this, R.string.font_brandon_bold);
        Toolbar toolbar = ButterKnife.findById(AnswersActivity.this, R.id.toolbar);
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
        RecyclerView recyclerView = ButterKnife.findById(AnswersActivity.this, R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                AnswersActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAnswersAdapter = new AnswersAdapter(AnswersActivity.this);
        mAnswersAdapter.setQuestionReplyListener(mQuestionReplyListener);
        recyclerView.setAdapter(mAnswersAdapter);
    }

    private final AnswersAdapter.OnQuestionReplyListener mQuestionReplyListener
            = new AnswersAdapter.OnQuestionReplyListener() {
        @Override public void onReply(Question question) {
            displayAnswerMakerDialog(question);
        }
    };

    private void displayAnswerMakerDialog(Question question) {
        AnswerDialog dialog = AnswerDialog.newInstance(getUserId(), question);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
        dialog.setAnswerListener(new AnswerDialog.OnAnswerListener() {
            @Override public void onAnswer(AnswerDialog dialog, Answer answer) {
                dialog.dismiss();
                mPresenter.makeAnswer(answer);
            }
        });
    }

    @Override protected void onStart() {
        super.onStart();
        fetchQuestions();
    }

    private void fetchQuestions() {
        mPresenter.fetchQuestionsByAdvertId(mAdvertId);
    }


    @Override public void showQuestionsInView(List<Question> questions) {
        mAnswersAdapter.clear();
        mAnswersAdapter.addQuestions(questions);
    }

    @Override public void showAnswerInView(Answer answer) {
        fetchQuestions();
    }

    private int getUserId() {
        AccountManager accountManager = AccountManager.get(AnswersActivity.this);
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.authenticator_account_type));
        String userId = accountManager.getUserData(accounts[0], getString(R.string.authenticator_user_id));
        return Integer.valueOf(userId);
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

    @Override public void setPresenter(@NonNull AnswersContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override protected void onStop() {
        super.onStop();
        mPresenter.pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mAnswersAdapter.clear();
    }
}
