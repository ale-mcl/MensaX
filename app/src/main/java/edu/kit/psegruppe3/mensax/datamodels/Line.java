package edu.kit.psegruppe3.mensax.datamodels;

import java.util.ArrayList;

public class Line {
    private String name;
    private ArrayList<Offer> offerList;

    public Line(String name, ArrayList<Offer> offerList) {
        this.name = name;
        this.offerList = offerList;
    }

    public void addOffer() {
    }

    public String getName(){
        return name;
    }

}
