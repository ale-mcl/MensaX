package edu.kit.psegruppe3.mensax.datamodels;

public class Offer {
    private Meal meal;
    private int price;

    public Offer(Meal meal, int price) {
        this.meal = meal;
        this. price = price;
    }

    public int getPrice(){
        return price;
    }

    public Meal getMeal() {
        return meal;
    }
}
