package edu.kit.psegruppe3.mensax.datamodels;

import android.net.Uri;

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

    public void rate(){}

    public void uploadPicture() {}

    public void mergeMeals() {}

    public void suche(){}

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

    public int getGlobalRating() {
        return globalRating;
    }

    public int getUserRating() {
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

    public void setGlobarRating(int globalRating) {
        this.globalRating = globalRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

}
