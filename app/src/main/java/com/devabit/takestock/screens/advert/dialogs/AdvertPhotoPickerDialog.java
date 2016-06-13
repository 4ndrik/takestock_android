package com.devabit.takestock.screens.advert.dialogs;

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
public class AdvertPhotoPickerDialog extends BottomSheetDialogFragment {

    public static AdvertPhotoPickerDialog newInstance() {
        return new AdvertPhotoPickerDialog();
    }

    private Unbinder mUnbinder;

    public interface OnPickListener {
        void onPickFromCamera(AdvertPhotoPickerDialog dialog);
        void onPickFromLibrary(AdvertPhotoPickerDialog dialog);
    }

    private OnPickListener mPickListener;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_advert_photo_picker, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(AdvertPhotoPickerDialog.this, view);
    }

    @OnClick(R.id.take_photo_text_view)
    protected void onTakePhotoButtonClick() {
        if (mPickListener != null) mPickListener.onPickFromCamera(AdvertPhotoPickerDialog.this);
    }

    @OnClick(R.id.choose_from_library_text_view)
    protected void onChooseButtonClick() {
        if (mPickListener != null) mPickListener.onPickFromLibrary(AdvertPhotoPickerDialog.this);
    }

    public void setOnPickListener(OnPickListener pickListener) {
        mPickListener = pickListener;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
