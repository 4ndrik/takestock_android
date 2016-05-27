package com.devabit.takestock.screens.advert.detail.dialogs;

import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.*;
import com.devabit.takestock.R;
import com.devabit.takestock.data.models.Advert;
import com.devabit.takestock.data.models.Offer;
import com.devabit.takestock.util.FontCache;

import java.util.List;

import static com.devabit.takestock.util.Logger.LOGD;
import static com.devabit.takestock.util.Logger.makeLogTag;

/**
 * Created by Victor Artemyev on 14/04/2016.
 */
public class OfferMakerDialog extends DialogFragment implements OfferMakerContract.View {

    private static final String TAG = makeLogTag(OfferMakerDialog.class);

    private static final String ARG_USER_ID = "USER_ID";
    private static final String ARG_ADVERT = "ADVERT";

    public static OfferMakerDialog newInstance(int userId, Advert advert) {
        Bundle args = new Bundle();
        OfferMakerDialog dialog = new OfferMakerDialog();
        args.putInt(ARG_USER_ID, userId);
        args.putParcelable(ARG_ADVERT, advert);
        dialog.setArguments(args);
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dialog.setCancelable(true);
        return dialog;
    }

    private int mUserId;
    private Advert mAdvert;

    public interface OnOfferMadeListener {
        void onOfferMade(OfferMakerDialog dialog, Offer offer);
    }

    private OnOfferMadeListener mOfferMadeListener;

    private Unbinder mUnbinder;
    private OfferMakerContract.Presenter mPresenter;

    @BindView(R.id.quantity_edit_text) protected EditText mQuantityEditText;
    @BindView(R.id.price_edit_text) protected EditText mPriceEditText;
    @BindView(R.id.total_price_text_view) protected TextView mTotalPriceTextView;
    @BindViews({R.id.make_offer_button, R.id.cancel_button})
    protected List<Button> mButtons;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mUserId = args.getInt(ARG_USER_ID);
        mAdvert = args.getParcelable(ARG_ADVERT);
        new OfferMakerPresenter(OfferMakerDialog.this);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_offer_maker, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(OfferMakerDialog.this, view);
        final Typeface boldTypeface = FontCache.getTypeface(getActivity(), R.string.font_brandon_bold);
        ButterKnife.apply(mButtons, new ButterKnife.Action<Button>() {
            @Override public void apply(@NonNull Button view, int index) {
                view.setTypeface(boldTypeface);
            }
        });
    }

    @Override public void onStart() {
        super.onStart();
        calculateTotalPrice();
    }

    @OnTextChanged(R.id.quantity_edit_text)
    protected void onQuantityTextChanged(CharSequence text) {
        LOGD(TAG, "onQuantityTextChanged: " + text);
        calculateTotalPrice();
    }

    @OnTextChanged(R.id.price_edit_text)
    protected void onPriceTextChanged(CharSequence text) {
        LOGD(TAG, "onPriceTextChanged: " + text);
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        mPresenter.calculateOfferTotalPrice(getQuantity(), getPrice());
    }

    @OnClick(R.id.make_offer_button)
    protected void onMakeOfferButtonClick() {
        Offer offer = createOffer();
        LOGD(TAG, "onMakeOfferButtonClick: " + offer);
        mPresenter.validateOffer(offer);
    }

    private Offer createOffer() {
        Offer offer = new Offer();
        offer.setAdvertId(mAdvert.getId());
        offer.setUserId(mUserId);
        offer.setQuantity(getQuantityAsInt());
        offer.setPrice(getPrice());
        offer.setOfferStatusId(1);
        return offer;
    }

    private int getQuantityAsInt() {
        if (getQuantity().isEmpty()) return 0;
        else return Integer.valueOf(getQuantity());
    }

    private String getQuantity() {
        return mQuantityEditText.getText().toString().trim();
    }

    private String getPrice() {
        return mPriceEditText.getText().toString().trim();
    }

    @Override public void showValidOffer(Offer offer) {
        if (mOfferMadeListener != null) mOfferMadeListener.onOfferMade(OfferMakerDialog.this, offer);
    }

    @Override public void showTotalPriceInView(String quantity, double totalPrice) {
        mTotalPriceTextView.setText(getString(R.string.total_price, quantity, totalPrice));
    }

    @Override public void showEmptyQuantityError() {
        mQuantityEditText.setError(getText(R.string.error_empty_quantity));
    }

    @Override public void showEmptyPriceError() {
        mPriceEditText.setError(getText(R.string.error_empty_price));
    }

    @OnClick(R.id.cancel_button)
    protected void onCancelButtonClick() {
        dismiss();
    }

    @Override public void setPresenter(@NonNull OfferMakerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void setOnOfferMadeListener(OnOfferMadeListener offerMadeListener) {
        mOfferMadeListener = offerMadeListener;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
