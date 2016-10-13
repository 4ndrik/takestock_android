package com.devabit.takestock.screen.profileEditor.dialog;

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
 * Created by Victor Artemyev on 08/06/2016.
 */
public class PhotoPickerDialog extends BottomSheetDialogFragment {

    public static PhotoPickerDialog newInstance() {
        return new PhotoPickerDialog();
    }

    private Unbinder mUnbinder;

    public interface OnPickPhotoListener {
        void pickFromCamera(PhotoPickerDialog dialog);
        void pickFromLibrary(PhotoPickerDialog dialog);
        void delete(PhotoPickerDialog dialog);
    }

    private OnPickPhotoListener mPickPhotoListener;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_profile_photo_picker, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(PhotoPickerDialog.this, view);
    }

    public void setOnPickPhotoListener(OnPickPhotoListener pickPhotoListener) {
        mPickPhotoListener = pickPhotoListener;
    }

    @OnClick(R.id.take_photo_text_view)
    protected void onTakePhotoTextViewClick() {
        if (lacksOnPickPhotoListener()) return;
        mPickPhotoListener.pickFromCamera(PhotoPickerDialog.this);
    }

    @OnClick(R.id.choose_from_library_text_view)
    protected void onChooseFromLibraryTextViewClick() {
        if (lacksOnPickPhotoListener()) return;
        mPickPhotoListener.pickFromLibrary(PhotoPickerDialog.this);
    }

//    @OnClick(R.id.delete_photo_text_view)
//    protected void onDeletePhotoTextViewClick() {
//        if (lacksOnPickPhotoListener()) return;
//        mPickPhotoListener.delete(ProfilePhotoPickerDialog.this);
//    }

    private boolean lacksOnPickPhotoListener() {
        return mPickPhotoListener == null;
    }

    @Override public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }
}
