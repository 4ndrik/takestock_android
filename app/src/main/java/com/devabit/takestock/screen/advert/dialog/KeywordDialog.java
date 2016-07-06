package com.devabit.takestock.screen.advert.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import com.devabit.takestock.R;

import static com.devabit.takestock.utils.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 04/07/2016.
 */
public class KeywordDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String TAG = makeLogTag(KeywordDialog.class);

    public static KeywordDialog newInstance() {
        return new KeywordDialog();
    }

    @BindView(R.id.keyword_edit_text) protected EditText mKeywordEditText;

    private Unbinder mUnbinder;
    private Button mPositiveButton;

    public interface OnKeywordListener {
        void onAdd(KeywordDialog dialog, String word);
    }

    private OnKeywordListener mKeywordListener;

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog_Alert_Purple);
        builder.setTitle(R.string.keyword);
        builder.setPositiveButton(R.string.add, KeywordDialog.this);
        builder.setNegativeButton(R.string.cancel, KeywordDialog.this);
        View content = getActivity().getLayoutInflater().inflate(R.layout.dialog_keyword, null);
        builder.setView(content);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        mUnbinder = ButterKnife.bind(KeywordDialog.this, content);
        return dialog;
    }

    @Override public void onStart() {
        super.onStart();
        mPositiveButton = (Button) getDialog().findViewById(android.R.id.button1);
        setPositiveButtonActive(false);
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

    @OnTextChanged(R.id.keyword_edit_text)
    protected void onKeywordChanged(CharSequence text) {
        setPositiveButtonActive(text.length() > 0);
    }

    private void setPositiveButtonActive(boolean isActive) {
        mPositiveButton.setEnabled(isActive);
        mPositiveButton.setAlpha(isActive ? 1.0f : 0.9f);
    }

    private void onPositiveButtonClick() {
        hideKeyboard();
        if (mKeywordListener != null) mKeywordListener.onAdd(KeywordDialog.this, getKeyword());
    }

    private String getKeyword() {
        return mKeywordEditText.getText().toString().trim();
    }

    private void onNegativeButtonClick() {
        hideKeyboard();
        dismiss();
    }

    private void hideKeyboard() {
        if(mKeywordEditText.hasFocus()) {
            InputMethodManager imm =  (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mKeywordEditText.getWindowToken(), 0);
        }
    }

    public void setOnKeywordListener(OnKeywordListener keywordListener) {
        mKeywordListener = keywordListener;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
