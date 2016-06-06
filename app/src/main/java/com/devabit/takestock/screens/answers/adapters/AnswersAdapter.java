package com.devabit.takestock.screens.answers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Answer;
import com.devabit.takestock.data.models.Question;

import java.util.ArrayList;
import java.util.List;

import static butterknife.ButterKnife.findById;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Question> mQuestions;

    public AnswersAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mQuestions = new ArrayList<>();
    }

    public interface OnQuestionReplyListener {
        void onReply(Question question);
    }

    private static OnQuestionReplyListener sQuestionReplyListener;

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_answer, parent, false);
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

    public void clear() {
        mQuestions.clear();
        notifyDataSetChanged();
    }

    public void setQuestionReplyListener(OnQuestionReplyListener questionReplyListener) {
        sQuestionReplyListener = questionReplyListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ViewSwitcher viewSwitcher;
        final TextView questionTextView;
        final TextView answerTextView;

        private Question mQuestion;

        public ViewHolder(View itemView) {
            super(itemView);
            viewSwitcher = findById(itemView, R.id.view_switcher);
            questionTextView = findById(itemView, R.id.question_text_view);
            answerTextView = findById(itemView, R.id.answer_text_view);
            Button replyButton = findById(itemView, R.id.reply_button);
            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (sQuestionReplyListener != null) sQuestionReplyListener.onReply(mQuestion);
                }
            });
        }

        void bindQuestion(Question question) {
            mQuestion = question;
            questionTextView.setText(question.getMessage());
            Answer answer = question.getAnswer();
            if (answer == null) {
                viewSwitcher.setDisplayedChild(0);
            } else {
                viewSwitcher.setDisplayedChild(1);
                answerTextView.setText(answer.getMessage());
            }
        }
    }

    public void destroy() {
        sQuestionReplyListener = null;
    }
}
