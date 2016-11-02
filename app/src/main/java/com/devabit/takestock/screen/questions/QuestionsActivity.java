package com.devabit.takestock.screen.questions;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Answer;
import com.devabit.takestock.data.model.Question;
import com.devabit.takestock.screen.questions.adapters.QuestionsAdapter;
import com.devabit.takestock.screen.questions.dialog.AnswerDialog;
import com.devabit.takestock.ui.decoration.DividerItemDecoration;

import java.util.List;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
public class QuestionsActivity extends AppCompatActivity implements QuestionsContract.View {

    private static final String EXTRA_ADVERT_ID = "ADVERT_ID";

    public static Intent getStartIntent(Context context, int advertId) {
        Intent starter = new Intent(context, QuestionsActivity.class);
        starter.putExtra(EXTRA_ADVERT_ID, advertId);
        return starter;
    }

    @BindView(R.id.content_activity_questions) protected View mContent;
    @BindView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mRefreshLayout;

    private QuestionsAdapter mQuestionsAdapter;
    private QuestionsContract.Presenter mPresenter;

    private boolean mAreQuestionsLoading;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        ButterKnife.bind(QuestionsActivity.this);
        setUpToolbar();
        setUpRefreshLayout();
        setUpQuestionRecyclerView();
        int advertId = getIntent().getIntExtra(EXTRA_ADVERT_ID, 0);
        createPresenter(advertId);
    }

    private void setUpToolbar() {
        Toolbar toolbar = ButterKnife.findById(QuestionsActivity.this, R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setEnabled(false);
    }

    private void setUpQuestionRecyclerView() {
        RecyclerView recyclerView = ButterKnife.findById(QuestionsActivity.this, R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(
                QuestionsActivity.this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                ContextCompat.getDrawable(QuestionsActivity.this, R.drawable.divider_grey300));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (dy > 0 || mAreQuestionsLoading) return;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    mPresenter.loadQuestions();
                }
            }
        });
        mQuestionsAdapter = new QuestionsAdapter(QuestionsActivity.this);
        mQuestionsAdapter.setOnQuestionRepliedListener(mQuestionRepliedListener);
        recyclerView.setAdapter(mQuestionsAdapter);
    }

    private final QuestionsAdapter.OnQuestionRepliedListener mQuestionRepliedListener
            = new QuestionsAdapter.OnQuestionRepliedListener() {
        @Override public void onReplied(Question question) {
            displayAnswerMakerDialog(question);
        }
    };

    private void displayAnswerMakerDialog(Question question) {
        AnswerDialog dialog = AnswerDialog.newInstance(question);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
        dialog.setAnswerListener(new AnswerDialog.OnAnswerListener() {
            @Override public void onAnswer(AnswerDialog dialog, Question question, Answer answer) {
                dialog.dismiss();
                mPresenter.makeAnswer(question, answer);
            }
        });
    }

    private void createPresenter(int advertId) {
        new QuestionsPresenter(advertId,
                Injection.provideDataRepository(QuestionsActivity.this), QuestionsActivity.this);
    }

    @Override public void setPresenter(@NonNull QuestionsContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.loadQuestions();
    }

    @Override public void showQuestionsInView(List<Question> questions) {
        mQuestionsAdapter.addQuestions(questions);
    }

    @Override public void showQuestionRepliedInView(Question question) {
        mQuestionsAdapter.refreshQuestion(question);
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
        mAreQuestionsLoading = isActive;
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
