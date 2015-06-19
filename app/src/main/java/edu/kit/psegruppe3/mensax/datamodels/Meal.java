package edu.kit.psegruppe3.mensax.datamodels;

import android.net.Uri;

/**
 * Created by ekremsenturk on 19.06.15.
 */
public class Meal {
    private String name;
    private int mealId;
    private Tag tag;
    private String ingredients;
    private int globalRating;
    private int userRating;
    private Uri[] images;

    public Meal(String name, int mealId) {
        this.name = name;
        this.mealId = mealId;
    }
}
