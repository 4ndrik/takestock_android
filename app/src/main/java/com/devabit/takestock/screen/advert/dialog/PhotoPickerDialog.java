package com.devabit.takestock.screen.advert.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.devabit.takestock.R;

/**
 * Created by Victor Artemyev on 11/05/2016.
 */
public class PhotoPickerDialog extends BottomSheetDialogFragment {

    public static PhotoPickerDialog newInstance() {
        return new PhotoPickerDialog();
    }

    private Unbinder mUnbinder;

    public interface OnPickListener {
        void onPickFromCamera(PhotoPickerDialog dialog);
        void onPickFromStorage(PhotoPickerDialog dialog);
    }

    private OnPickListener mPickListener;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_photo_picker, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(PhotoPickerDialog.this, view);
    }

    @OnClick(R.id.take_photo_text_view)
    protected void onTakePhotoButtonClick() {
        if (mPickListener != null) mPickListener.onPickFromCamera(PhotoPickerDialog.this);
    }

    @OnClick(R.id.choose_from_library_text_view)
    protected void onChooseButtonClick() {
        if (mPickListener != null) mPickListener.onPickFromStorage(PhotoPickerDialog.this);
    }

    public void setOnPickListener(OnPickListener pickListener) {
        mPickListener = pickListener;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
