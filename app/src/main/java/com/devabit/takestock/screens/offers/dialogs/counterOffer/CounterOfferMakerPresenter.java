package com.devabit.takestock.screens.offers.dialogs.counterOffer;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.models.Offer;

import static com.devabit.takestock.utils.Logger.LOGE;
import static com.devabit.takestock.utils.Logger.makeLogTag;
import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
public class CounterOfferMakerPresenter implements CounterOfferMakerContract.Presenter {

    private static final String TAG = makeLogTag(CounterOfferMakerPresenter.class);

    private final CounterOfferMakerContract.View mOfferMakerView;

    public CounterOfferMakerPresenter(@NonNull CounterOfferMakerContract.View offerMakerView) {
        mOfferMakerView = checkNotNull(offerMakerView, "offerMakerView cannot be null.");
        mOfferMakerView.setPresenter(CounterOfferMakerPresenter.this);
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
