package edu.kit.psegruppe3.mensax.datamodels;

public class Offer {

    public static final int PRICE_STUDENTS = 0;
    public static final int PRICE_GUESTS = 1;
    public static final int PRICE_STAFF = 2;
    public static final int PRICE_PUPILS = 3;

    private Meal meal;
    private int priceStudents;
    private int priceGuests;
    private int priceStaff;
    private int pricePupils;
    private Line line;

    public Offer(Meal meal, Line line, int priceStudents, int priceGuests, int priceStaff, int pricePupils) {
        this.meal = meal;
        this.priceStudents = priceStudents;
        this.priceGuests = priceGuests;
        this.priceStaff = priceStaff;
        this.pricePupils = pricePupils;
        this.line = line;
    }

    public int getPrice(int priceTag){
        switch (priceTag) {
            case PRICE_STUDENTS: return priceStudents;
            case PRICE_GUESTS: return priceGuests;
            case PRICE_STAFF: return  priceStaff;
            case PRICE_PUPILS: return pricePupils;
            default: return 0;
        }
    }

    public Meal getMeal() {
        return meal;
    }

    public Line getLine() {
        return line;
    }
}
