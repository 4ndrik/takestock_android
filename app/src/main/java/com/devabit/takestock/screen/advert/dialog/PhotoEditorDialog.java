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
 * Created by Victor Artemyev on 06/10/2016.
 */

public class PhotoEditorDialog extends BottomSheetDialogFragment {

    public static PhotoEditorDialog newInstance() {
        return new PhotoEditorDialog();
    }

    private Unbinder mUnbinder;

    public interface OnEditListener {
        void onRemove();
        void onTake();
        void onChoose();
    }

    private OnEditListener mEditListener;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_photo_editor, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(PhotoEditorDialog.this, view);
    }

    @OnClick(R.id.remove_photo_text_view)
    void onRemovePhotoButtonClick() {
        dismiss();
        if (mEditListener != null) mEditListener.onRemove();
    }

    @OnClick(R.id.take_photo_text_view)
    void onTakePhotoButtonClick() {
        dismiss();
        if (mEditListener != null) mEditListener.onTake();
    }

    @OnClick(R.id.choose_from_library_text_view)
    void onChooseButtonClick() {
        dismiss();
        if (mEditListener != null) mEditListener.onChoose();
    }

    public void setOnEditListener(OnEditListener pickListener) {
        mEditListener = pickListener;
    }

    @Override public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }
}
