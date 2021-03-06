package com.devabit.takestock.screen.advert.detail.dialogs;

import android.support.annotation.NonNull;
import com.devabit.takestock.data.model.Offer;
import timber.log.Timber;

import static com.devabit.takestock.utils.Preconditions.checkNotNull;

/**
 * Created by Victor Artemyev on 25/05/2016.
 */
final class OfferPresenter implements OfferContract.Presenter {


    private final OfferContract.View mOfferView;

    OfferPresenter(@NonNull OfferContract.View offerView) {
        mOfferView = checkNotNull(offerView, "offerView cannot be null.");
        mOfferView.setPresenter(OfferPresenter.this);
    }

    @Override public void resume() {

    }

    @Override public void validateOffer(Offer offer) {
        if (isOfferValid(offer)) {
            mOfferView.showValidOffer(offer);
        }
    }

    @Override public void calculateOfferTotalPrice(String quantity, String price) {
        quantity = quantity.isEmpty() ? "0" : quantity;
        price =  price.isEmpty() ? "0.0" : price;
        try {
            int qtyValue = Integer.valueOf(quantity);
            double priceValue = Double.valueOf(price);
            double totalPrice = qtyValue * priceValue;
            mOfferView.showTotalPriceInView(quantity, totalPrice);
        } catch (NumberFormatException e) {
            Timber.e(e);
        }
    }

    private boolean isOfferValid(Offer offer) {
        return validateQuantity(offer)
                && validatePrice(offer);
    }

    private boolean validateQuantity(Offer offer) {
        int quantity = offer.getQuantity();
        if (quantity <= 0) {
            mOfferView.showEmptyQuantityError();
            return false;
        }
        return true;
    }

    private boolean validatePrice(Offer offer) {
        String price = offer.getPrice();
        if (price.isEmpty()) {
            mOfferView.showEmptyPriceError();
            return false;
        }
        return true;
    }

    @Override public void pause() {

    }

    @Override public void destroy() {

    }
}
