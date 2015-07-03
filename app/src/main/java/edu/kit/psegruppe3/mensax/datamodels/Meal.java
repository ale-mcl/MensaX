package edu.kit.psegruppe3.mensax.datamodels;

import android.net.Uri;

public class Meal {
    private String name;
    private int mealId;
    private Tag tag;
    private String ingredients;
    private float globalRating;
    private float userRating;
    private Uri[] images;

    public Meal(String name, int mealId) {
        this.name = name;
        this.mealId = mealId;
    }


    public String getName(){
        return name;
    }

    public int getMealId() {
        return mealId;
    }

    public Tag getTag(){
        return tag;
    }

    public String getIngredients() {
        return ingredients;
    }

    public float getGlobalRating() {
        return globalRating;
    }

    public float getUserRating() {
        return userRating;
    }

    public Uri[] getImages() {
        return images;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setGlobalRating(float globalRating) {
        this.globalRating = globalRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

}
