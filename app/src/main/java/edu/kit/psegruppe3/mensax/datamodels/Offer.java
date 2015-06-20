package edu.kit.psegruppe3.mensax.datamodels;

public class Offer {
    private Meal meal;
    private int price;
    private Line line;

    public Offer(Meal meal, Line line, int price) {
        this.meal = meal;
        this.price = price;
        this.line = line;
    }

    public int getPrice(){
        return price;
    }

    public Meal getMeal() {
        return meal;
    }

    public Line getLine() {
        return line;
    }
}
