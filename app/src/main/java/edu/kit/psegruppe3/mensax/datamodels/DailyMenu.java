package edu.kit.psegruppe3.mensax.datamodels;

import java.util.ArrayList;

public class DailyMenu {
    private long timestamp;
    private Offer[] offers;

    public DailyMenu(long timestamp, Offer[] offers) {
        this.timestamp = timestamp;
        this.offers = offers;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public Offer getOffer(int position) {
        if (position < offers.length) {
            return offers[position];
        }
        return null;
    }

    public int getOfferCount() {
        return offers.length;
    }
}
