package com.devabit.takestock.screen.advert.selling.fragment.questions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.devabit.takestock.Injection;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Answer;
import com.devabit.takestock.data.model.Question;
import com.devabit.takestock.ui.decoration.DividerItemDecoration;

import java.util.List;

/**
 * Created by Victor Artemyev on 30/09/2016.
 */

public class QuestionsFragment extends Fragment implements QuestionsContract.View {

    private static final String ARG_ADVERT_ID = "ARG_ADVERT_ID";

    public static QuestionsFragment newInstance(int advertId) {
        Bundle args = new Bundle();
        args.putInt(ARG_ADVERT_ID, advertId);
        QuestionsFragment fragment = new QuestionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    SwipeRefreshLayout mSwipeRefreshLayout;

    QuestionsAdapter mQuestionsAdapter;
    QuestionsContract.Presenter mPresenter;

    private boolean mAreQuestionsLoading;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setUpSwipeRefreshLayout(view);
        setUpRecyclerView(view);
        createPresenter(view);
    }

    private void setUpSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = ButterKnife.findById(view, R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setEnabled(false);
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recyclerView = ButterKnife.findById(view, R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                ContextCompat.getDrawable(view.getContext(), R.drawable.divider_grey300));
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
        mQuestionsAdapter = new QuestionsAdapter(view.getContext());
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

    private void createPresenter(View view) {
        int advertId = getArguments().getInt(ARG_ADVERT_ID);
        new QuestionsPresenter(advertId, Injection.provideDataRepository(view.getContext()), QuestionsFragment.this);
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
        Snackbar.make(mSwipeRefreshLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void setProgressIndicator(boolean isActive) {
        mAreQuestionsLoading = isActive;
        mSwipeRefreshLayout.setRefreshing(isActive);
    }

    @Override public void onPause() {
        mPresenter.pause();
        super.onPause();
    }
}
