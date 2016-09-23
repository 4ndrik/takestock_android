package com.devabit.takestock.screen.questions.dialog;

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
import com.devabit.takestock.TakeStockAccount;
import com.devabit.takestock.data.model.Answer;
import com.devabit.takestock.data.model.Question;

/**
 * Created by Victor Artemyev on 02/06/2016.
 */
public class AnswerDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String ARG_QUESTION = "QUESTION";

    public static AnswerDialog newInstance(Question question) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_QUESTION, question);
        AnswerDialog fragment = new AnswerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.user_name_text_view) protected TextView mUserNameTextView;
    @BindView(R.id.question_text_view) protected TextView mQuestionTextView;
    @BindView(R.id.answer_edit_text) protected EditText mAnswerEditText;

    private Question mQuestion;
    private Unbinder mUnbinder;

    public interface OnAnswerListener {
        void onAnswer(AnswerDialog dialog, Question question, Answer answer);
    }

    private OnAnswerListener mAnswerListener;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestion = getArguments().getParcelable(ARG_QUESTION);
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog_Alert_Purple);
        builder.setPositiveButton(R.string.answer_dialog_reply, AnswerDialog.this);
        builder.setNegativeButton(R.string.answer_dialog_cancel, AnswerDialog.this);
        View content = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_answer, null);
        builder.setView(content);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        mUnbinder = ButterKnife.bind(AnswerDialog.this, content);
        return dialog;
    }

    @Override public void onStart() {
        super.onStart();
        mUserNameTextView.setText(mQuestion.getUserName());
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
            if (mAnswerListener != null) mAnswerListener.onAnswer(AnswerDialog.this, mQuestion, createAnswer());
        }
    }

    private boolean validateAnswer() {
        if (getAnswer().isEmpty()) {
            mAnswerEditText.setError(getString(R.string.answer_dialog_error_answer));
            return false;
        }
        return true;
    }

    private Answer createAnswer() {
        return new Answer.Builder()
                .setUserId(getUserId())
                .setMessage(getAnswer())
                .setQuestion(new int[]{mQuestion.getId()})
                .create();
    }

    private int getUserId() {
        return TakeStockAccount.get(getActivity()).getUserId();
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
        mUnbinder.unbind();
        super.onDestroy();
    }
}
