package edu.kit.psegruppe3.mensax.datamodels;

import android.net.Uri;

public class Meal {
    public static final int TAG_BIO = 0;
    public static final int TAG_FISH = 1;
    public static final int TAG_PORK = 2;
    public static final int TAG_COW = 3;
    public static final int TAG_COW_AW = 4;
    public static final int TAG_VEGAN = 5;
    public static final int TAG_VEG = 6;

    private String name;
    private int mealId;
    private boolean bio;
    private boolean fish;
    private boolean pork;
    private boolean cow;
    private boolean cow_aw;
    private boolean vegan;
    private boolean veg;
    private String ingredients;
    private double globalRating;
    private double userRating;
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

    public boolean hasTag(int index){
        switch (index) {
            case TAG_BIO: return bio;
            case TAG_FISH: return fish;
            case TAG_PORK: return pork;
            case TAG_COW: return cow;
            case TAG_COW_AW: return cow_aw;
            case TAG_VEGAN: return vegan;
            case TAG_VEG: return veg;
            default: return false;
        }
    }

    public String getIngredients() {
        return ingredients;
    }

    public double getGlobalRating() {
        return globalRating;
    }

    public double getUserRating() {
        return userRating;
    }

    public Uri[] getImages() {
        return images;
    }

    public void setTag(int index, boolean value){
        switch (index) {
            case TAG_BIO: bio = value; return;
            case TAG_FISH: fish = value; return;
            case TAG_PORK: pork = value; return;
            case TAG_COW: cow = value; return;
            case TAG_COW_AW: cow_aw = value; return;
            case TAG_VEGAN: vegan = value; return;
            case TAG_VEG: veg = value;
        }
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setGlobalRating(double globalRating) {
        this.globalRating = globalRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

}
