package com.devabit.takestock.screens.advert.create.dialogs;

import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.devabit.takestock.R;
import com.devabit.takestock.util.FontCache;

import java.util.List;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public class AdvertPhotoPickerDialog extends DialogFragment {

    public static AdvertPhotoPickerDialog newInstance() {
        AdvertPhotoPickerDialog dialog = new AdvertPhotoPickerDialog();
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return dialog;
    }

    @BindViews({R.id.take_photo_button, R.id.choose_photo_button, R.id.cancel_button})
    protected List<Button> mButtons;

    private Unbinder mUnbinder;

    public interface OnPickListener {
        void onPickFromLibrary(AdvertPhotoPickerDialog dialog);

        void onPickFromCamera(AdvertPhotoPickerDialog dialog);
    }

    private OnPickListener mPickListener;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_photo_picker, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setGravity(Gravity.CENTER | Gravity.BOTTOM);
        mUnbinder = ButterKnife.bind(AdvertPhotoPickerDialog.this, view);

        final Typeface boldTypeface = FontCache.getTypeface(getActivity(), R.string.font_brandon_bold);
        ButterKnife.apply(mButtons, new ButterKnife.Action<Button>() {
            @Override public void apply(@NonNull Button view, int index) {
                view.setTypeface(boldTypeface);
            }
        });
    }

    @OnClick(R.id.take_photo_button)
    protected void onTakePhotoButtonClick() {
        if (mPickListener != null) mPickListener.onPickFromCamera(AdvertPhotoPickerDialog.this);
    }

    @OnClick(R.id.choose_photo_button)
    protected void onChooseButtonClick() {
        if (mPickListener != null) mPickListener.onPickFromLibrary(AdvertPhotoPickerDialog.this);
    }

    @OnClick(R.id.cancel_button)
    protected void onCancelButtonClick() {
        dismiss();
    }

    public void setOnPickListener(OnPickListener pickListener) {
        mPickListener = pickListener;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
