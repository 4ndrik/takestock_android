package com.devabit.takestock.screen.answers.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.devabit.takestock.R;
import com.devabit.takestock.data.model.Answer;
import com.devabit.takestock.data.model.Question;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
public class AnswerDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String ARG_USER_ID = "USER_ID";
    private static final String ARG_QUESTION = "QUESTION";

    public static AnswerDialog newInstance(int userId, Question question) {

        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        args.putParcelable(ARG_QUESTION, question);
        AnswerDialog fragment = new AnswerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.question_text_view) protected TextView mQuestionTextView;
    @BindView(R.id.answer_edit_text) protected EditText mAnswerEditText;

    private int mUserId;
    private Question mQuestion;
    private Unbinder mUnbinder;

    public interface OnAnswerListener {
        void onAnswer(AnswerDialog dialog, Answer answer);
    }

    private OnAnswerListener mAnswerListener;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserId = getArguments().getInt(ARG_USER_ID);
        mQuestion = getArguments().getParcelable(ARG_QUESTION);
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog_Alert_Purple);
        builder.setTitle(R.string.answer);
        builder.setPositiveButton(R.string.reply, AnswerDialog.this);
        builder.setNegativeButton(R.string.cancel, AnswerDialog.this);
        View content = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_answer, null);
        builder.setView(content);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        mUnbinder = ButterKnife.bind(AnswerDialog.this, content);
        return dialog;
    }

    @Override public void onStart() {
        super.onStart();
        mQuestionTextView.setText(mQuestion.getMessage());
    }

    @Override public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                onPositiveButtonClick();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                onNegativeButtonClick();
                break;
        }
    }

    private void onPositiveButtonClick() {
        if (validateAnswer()) {
            if (mAnswerListener != null) mAnswerListener.onAnswer(AnswerDialog.this, createAnswer());
        }
    }

    private boolean validateAnswer() {
        if (getAnswer().isEmpty()) {
            mAnswerEditText.setError("Add answer.");
            return false;
        }
        return true;
    }

    private Answer createAnswer() {
        Answer answer = new Answer();
        answer.setUserId(mUserId);
        answer.setMessage(getAnswer());
        int[] questionIds = new int[]{mQuestion.getId()};
        answer.setQuestionSet(questionIds);
        return answer;
    }

    private String getAnswer() {
        return mAnswerEditText.getText().toString().trim();
    }

    private void onNegativeButtonClick() {
        dismiss();
    }

    public void setAnswerListener(OnAnswerListener answerListener) {
        mAnswerListener = answerListener;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
