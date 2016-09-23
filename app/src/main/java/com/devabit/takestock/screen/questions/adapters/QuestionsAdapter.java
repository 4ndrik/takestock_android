package com.devabit.takestock.screen.questions.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Answer;
import com.devabit.takestock.data.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private static final int TYPE_QUESTION = 1;
    private static final int TYPE_QUESTION_REPLIED = 2;

    private final LayoutInflater mLayoutInflater;
    private final List<Question> mQuestions;

    public QuestionsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mQuestions = new ArrayList<>();
    }

    public interface OnQuestionRepliedListener {
        void onReplied(Question question);
    }

    private OnQuestionRepliedListener mRepliedListener;

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_QUESTION_REPLIED:
                return new QuestionRepliedViewHolder(inflateItemView(R.layout.item_question_replied, parent));

            default:
                return new QuestionViewHolder(inflateItemView(R.layout.item_question, parent));
        }
    }

    private View inflateItemView(@LayoutRes int resId, ViewGroup parent) {
        return mLayoutInflater.inflate(resId, parent, false);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_QUESTION:
                ((QuestionViewHolder) holder).bindQuestion(mQuestions.get(position));
                break;

            case TYPE_QUESTION_REPLIED:
                ((QuestionRepliedViewHolder) holder).bindQuestion(mQuestions.get(position));
                break;
        }
    }

    @Override public int getItemCount() {
        return mQuestions.size();
    }

    @Override public int getItemViewType(int position) {
        Question question = mQuestions.get(position);
        if (question.hasAnswer()) return TYPE_QUESTION_REPLIED;
        else return TYPE_QUESTION;
    }

    public void addQuestions(List<Question> questions) {
        int positionStart = mQuestions.size();
        mQuestions.addAll(questions);
        notifyItemRangeInserted(positionStart, questions.size());
    }

    public void refreshQuestion(Question question) {
        int position = mQuestions.indexOf(question);
        mQuestions.set(position, question);
        notifyItemChanged(position);
    }

    public void setOnQuestionRepliedListener(OnQuestionRepliedListener onQuestionRepliedListener) {
        mRepliedListener = onQuestionRepliedListener;
    }

    class QuestionRepliedViewHolder extends ViewHolder {

        @BindView(R.id.user_name_text_view) TextView userNameTextView;
        @BindView(R.id.question_text_view) TextView questionTextView;
        @BindView(R.id.answer_text_view) TextView answerTextView;

        private Question mQuestion;

        QuestionRepliedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(QuestionRepliedViewHolder.this, itemView);
        }

        void bindQuestion(Question question) {
            mQuestion = question;
            userNameTextView.setText(mQuestion.getUserName());
            questionTextView.setText(mQuestion.getMessage());
            Answer answer = mQuestion.getAnswer();
            answerTextView.setText(answer.getMessage());
        }
    }

    class QuestionViewHolder extends ViewHolder {

        @BindView(R.id.user_name_text_view) TextView userNameTextView;
        @BindView(R.id.question_text_view) TextView questionTextView;

        private Question mQuestion;

        QuestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(QuestionViewHolder.this, itemView);
        }

        void bindQuestion(Question question) {
            mQuestion = question;
            userNameTextView.setText(question.getUserName());
            questionTextView.setText(question.getMessage());
        }

        @OnClick(R.id.reply_button)
        void onReplyButtonClick() {
            if (mRepliedListener != null) mRepliedListener.onReplied(mQuestion);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
