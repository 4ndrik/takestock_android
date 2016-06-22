package com.devabit.takestock.screen.questions.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Answer;
import com.devabit.takestock.data.models.Question;

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
        notifyDataSetChanged();
    }

    public void clearQuestions() {
        mQuestions.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView questionTextView;
        final TextView answerTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            questionTextView = ButterKnife.findById(itemView, R.id.question_text_view);
            answerTextView = ButterKnife.findById(itemView, R.id.answer_text_view);
        }

        void bindQuestion(Question question) {
            questionTextView.setText(question.getMessage());
            Answer answer = question.getAnswer();
            if (answer == null) {
//                setAnswerTextViewVisibility(false);
                answerTextView.setText("There should be an answer.");
            } else {
//                setAnswerTextViewVisibility(true);
                answerTextView.setText(answer.getMessage());
            }
        }

        void setAnswerTextViewVisibility(boolean isVisible) {
            answerTextView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
}
