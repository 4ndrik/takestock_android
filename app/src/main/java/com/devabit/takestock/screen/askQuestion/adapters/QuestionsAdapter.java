package com.devabit.takestock.screen.askQuestion.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Answer;
import com.devabit.takestock.data.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 30/05/2016.
 */
public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Question> mQuestions;

    public QuestionsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mQuestions = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindQuestion(mQuestions.get(position));
    }

    @Override public int getItemCount() {
        return mQuestions.size();
    }

    public void addQuestions(List<Question> questions) {
        mQuestions.addAll(questions);
        notifyDataSetChanged();
    }

    public void addQuestion(Question question) {
        mQuestions.add(question);
        notifyItemInserted(mQuestions.size());
    }

    public void refreshQuestions(List<Question> questions) {
        mQuestions.clear();
        mQuestions.addAll(questions);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.question_text_view) TextView questionTextView;
        @BindView(R.id.answer_text_view) TextView answerTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(ViewHolder.this, itemView);
        }

        void bindQuestion(Question question) {
            questionTextView.setText(question.getMessage());
            Answer answer = question.getAnswer();
            if (answer == null) {
                setAnswerTextViewVisibility(false);
            } else {
                setAnswerTextViewVisibility(true);
                answerTextView.setText(answer.getMessage());
            }
        }

        void setAnswerTextViewVisibility(boolean isVisible) {
            answerTextView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
}
