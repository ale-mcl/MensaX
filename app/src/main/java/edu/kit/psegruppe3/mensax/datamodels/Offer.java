package edu.kit.psegruppe3.mensax.datamodels;

/**
 * Class of the Offer objects that represents an offer of the Mensa.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class Offer {

    /**
     * The price of a meal for a student
     */
    public static final int PRICE_STUDENTS = 0;
    /**
     * The price of a meal for a guest
     */
    public static final int PRICE_GUESTS = 1;
    /**
     * The price of a meal for a member of the staff
     */
    public static final int PRICE_STAFF = 2;
    /**
     * The price of a meal for a pupil
     */
    public static final int PRICE_PUPILS = 3;

    private Meal meal;
    private int priceStudents;
    private int priceGuests;
    private int priceStaff;
    private int pricePupils;
    private Line line;

    /**
     * Constructor of an Offer object
     * @param meal the meal of the offer
     * @param line the line in which the meal is avaible
     * @param priceStudents the price for the students
     * @param priceGuests the price for the guests
     * @param priceStaff the price for the staff members
     * @param pricePupils the price for the pupils
     */
    public Offer(Meal meal, Line line, int priceStudents, int priceGuests, int priceStaff, int pricePupils) {
        this.meal = meal;
        this.priceStudents = priceStudents;
        this.priceGuests = priceGuests;
        this.priceStaff = priceStaff;
        this.pricePupils = pricePupils;
        this.line = line;
    }

    /**
     * Method to get the price of an offer for a specific category of customers.
     * @param priceTag the customer category
     * @return the price of the offer
     */
    public int getPrice(int priceTag){
        switch (priceTag) {
            case PRICE_STUDENTS: return priceStudents;
            case PRICE_GUESTS: return priceGuests;
            case PRICE_STAFF: return  priceStaff;
            case PRICE_PUPILS: return pricePupils;
            default: return 0;
        }
    }

    /**
     * Method to get the meal object of an offer.
     * @return the meal object of an offer
     */
    public Meal getMeal() {
        return meal;
    }

    /**
     * Method to get the line in which an offer is avaible
     * @return the line parameter
     */
    public Line getLine() {
        return line;
    }
}
