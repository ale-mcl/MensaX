package edu.kit.psegruppe3.mensax.datamodels;

/**
 * Class Daily Menu that contains and manages offers objects.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class DailyMenu {
    private Offer[] offers;

    /**
     * Constructor for the class.
     * @param offers array of onjects
     */
    public DailyMenu(Offer[] offers) {
        this.offers = offers;
    }

    /**
     * Method to get a single offer.
     * @param position the position of the offer in the array
     * @return  the requested offer
     */
    public Offer getOffer(int position) {
        if (position < offers.length) {
            return offers[position];
        }
        return null;
    }

    /**
     * Method to get the ammount of offer objects.
     * @return integer ammount of the object
     */
    public int getOfferCount() {
        return offers.length;
    }
}
