package com.devabit.takestock.screen.profileEditor.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.devabit.takestock.R;

/**
 * Created by Victor Artemyev on 13/10/2016.
 */

public class PasswordDialog extends DialogFragment {

    private static final String KEY_CURRENT_PASS = "CURRENT_PASS";
    private static final int MIN_LENGTH_PASSWORD = 6;

    public static PasswordDialog newInstance(String password) {
        Bundle args = new Bundle();
        args.putString(KEY_CURRENT_PASS, password);
        PasswordDialog fragment = new PasswordDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private String mCurrentPass;

    public interface OnPasswordChangeListener {
        void onPasswordChanged(String newPassword);
    }

    private OnPasswordChangeListener mOnPasswordChangeListener;

    @BindView(R.id.current_password_edit_text) protected EditText mCurrentPassEditText;
    @BindView(R.id.new_password_edit_text) protected EditText mNewPassEditText;
    @BindView(R.id.password_check_box) protected CheckBox mCheckBox;
    @BindView(R.id.wrong_password_error_text_view) protected TextView mWrongPassErrorTextView;
    @BindView(R.id.password_length_error_text_view) protected TextView mPassLengthErrorTextView;

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCurrentPass = getArguments().getString(KEY_CURRENT_PASS);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.password);
        builder.setView(R.layout.dialog_password);
        return builder.create();
    }

    @Override public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        View content = ButterKnife.findById(dialog, R.id.content);
        ButterKnife.bind(PasswordDialog.this, content);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mCurrentPassEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mNewPassEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mCurrentPassEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mNewPassEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private String getCurrentPassword() {
        return mCurrentPassEditText.getText().toString().trim();
    }

    public void setOnPasswordChangeListener(OnPasswordChangeListener onPasswordChangeListener) {
        mOnPasswordChangeListener = onPasswordChangeListener;
    }

    @OnTextChanged(R.id.current_password_edit_text)
    void onCurrentPasswordTextChanged(CharSequence text) {
        boolean isValid = isCurrentPasswordValid(text.toString());
        mNewPassEditText.setEnabled(isValid);
        mNewPassEditText.setAlpha(isValid ? 1f : 0.5f);
        if (mWrongPassErrorTextView.isShown()) {
            mWrongPassErrorTextView.setVisibility(View.GONE);
        }
    }

    @OnTextChanged(R.id.new_password_edit_text)
    void onNewPasswordTextChanged() {
        if (mPassLengthErrorTextView.isShown()) {
            mPassLengthErrorTextView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.cancel_button)
    void onCancelButtonClick() {
        dismiss();
    }

    @OnClick(R.id.ok_button)
    void onOkButtonClick() {
        if (!isCurrentPasswordValid(getCurrentPassword())) {
            mWrongPassErrorTextView.setVisibility(View.VISIBLE);
            return;
        }

        if (getNewPassword().length() < MIN_LENGTH_PASSWORD) {
            mPassLengthErrorTextView.setVisibility(View.VISIBLE);
        } else {
            if (mOnPasswordChangeListener != null) mOnPasswordChangeListener.onPasswordChanged(getNewPassword());
            dismiss();
        }
    }

    private boolean isCurrentPasswordValid(String text) {
        return mCurrentPass.equals(text);
    }

    private String getNewPassword() {
        return mNewPassEditText.getText().toString().trim();
    }
}
