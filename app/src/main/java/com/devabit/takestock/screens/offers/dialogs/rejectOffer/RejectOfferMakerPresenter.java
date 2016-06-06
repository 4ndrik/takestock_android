package com.devabit.takestock.screens.offers.dialogs.rejectOffer;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.models.Offer;

import static com.devabit.takestock.util.Logger.LOGE;
import static com.devabit.takestock.util.Logger.makeLogTag;
import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class RejectOfferMakerPresenter implements RejectOfferMakerContract.Presenter {

    private static final String TAG = makeLogTag(RejectOfferMakerPresenter.class);

    private final RejectOfferMakerContract.View mOfferMakerView;

    public RejectOfferMakerPresenter(@NonNull RejectOfferMakerContract.View offerMakerView) {
        mOfferMakerView = checkNotNull(offerMakerView, "offerMakerView cannot be null.");
        mOfferMakerView.setPresenter(RejectOfferMakerPresenter.this);
    }

    @Override public void create() {

    }

    @Override public void resume() {

    }

    @Override public void validateOffer(Offer offer) {
        if (isOfferValid(offer)) {
            mOfferMakerView.showValidOffer(offer);
        }
    }

    @Override public void calculateOfferTotalPrice(String quantity, String price) {
        quantity = quantity.isEmpty() ? "0" : quantity;
        price =  price.isEmpty() ? "0.0" : price;
        try {
            int qtyValue = Integer.valueOf(quantity);
            double priceValue = Double.valueOf(price);
            double totalPrice = qtyValue * priceValue;
            mOfferMakerView.showTotalPriceInView(quantity, totalPrice);
        } catch (NumberFormatException e) {
            LOGE(TAG, "BOOM:", e);
        }
    }

    private boolean isOfferValid(Offer offer) {
        return validateQuantity(offer)
                && validatePrice(offer);
    }

    private boolean validateQuantity(Offer offer) {
        int quantity = offer.getQuantity();
        if (quantity <= 0) {
            mOfferMakerView.showEmptyQuantityError();
            return false;
        }
        return true;
    }

    private boolean validatePrice(Offer offer) {
        String price = offer.getPrice();
        if (price.isEmpty()) {
            mOfferMakerView.showEmptyPriceError();
            return false;
        }
        return true;
    }

    @Override public void pause() {

    }

    @Override public void destroy() {

    }
}
