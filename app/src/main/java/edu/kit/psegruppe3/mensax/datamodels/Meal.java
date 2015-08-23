package edu.kit.psegruppe3.mensax.datamodels;

/**
 * Class that describes a Meal object.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class Meal {
    /**
     * Integer for the TAG_BIO.
     */
    public static final int TAG_BIO = 0;
    /**
     * Integer for the TAG_FISCH.
     */
    public static final int TAG_FISH = 1;
    /**
     * Integer for the TAG_PORK.
     */
    public static final int TAG_PORK = 2;
    /**
     * Integer for the TAG_COW.
     */
    public static final int TAG_COW = 3;
    /**
     * Integer for the TAG_COW_AW.
     */
    public static final int TAG_COW_AW = 4;
    /**
     * Integer for the TAG_VEGAN.
     */
    public static final int TAG_VEGAN = 5;
    /**
     * Integer for the TAG_VEG.
     */
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
    private String[] images;

    /**
     * The constructor of a Meal object.
     *
     * @param name the name of the meal
     * @param mealId the unique id of a meal
     */
    public Meal(String name, int mealId) {
        this.name = name;
        this.mealId = mealId;
    }


    /**
     * Method to get the name of a meal.
     * @return the name of the meal
     */
    public String getName(){
        return name;
    }

    /**
     * Methode to get the unique id of a meal.
     * @return the id of a meal
     */
    public int getMealId() {
        return mealId;
    }

    /**
     * Method to set an array of urls that points to the pictures of a meal
     * @param imageUris the array of urls
     */
    public void setImages(String[] imageUris) {
        images = imageUris;
    }

    /**
     * Method that checks if a meal has a specific tag.
     * @param index the tag index to check on a meal
     * @return true if meal has the tag
     */
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

    /**
     * Method to get the ingredients of a meal.
     * @return a string with all ingredients
     */
    public String getIngredients() {
        return ingredients;
    }

    /**
     * Method to get the global rating of a meal.
     * @return the global rating of a meal
     */
    public double getGlobalRating() {
        return globalRating;
    }

    /**
     * Method to get the rating of a user for a meal.
     * @return the user rating of a meal
     */
    public double getUserRating() {
        return userRating;
    }

    /**
     * Method to get the String that cointains the urls to all pictures of a meal.
     * @return string with urls.
     */
    public String[] getImages() {
        return images;
    }

    /**
     * Method to set if a meal has a specific Tag.
     * @param index the tag to be set
     * @param value true if meal cointains the tag
     */
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

    /**
     * Method to set the ingredients of a meal.
     * @param ingredients string with all ingredients
     */
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Method to set the global rating of a meal.
     * @param globalRating value of the global rating
     */
    public void setGlobalRating(double globalRating) {
        this.globalRating = globalRating;
    }

    /**
     * Method to set the user rating of a meal.
     * @param userRating value of the user rating
     */
    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

}
