package edu.kit.psegruppe3.mensax;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Classes provides conventions for the communication between client and server.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class ServerApiContract {
    /**
     * The base URL of the API.
     */
    public static final String BASE_URL = "https://i43pc164.ipd.kit.edu/PSESoSe15Gruppe3/mensa/api";

    /**
     * Path of the meal getter API.
     */
    public static final String PATH_MEAL = "meal";
    /**
     * Path of the plan getter API.
     */
    public static final String PATH_PLAN = "plan";
    /**
     * Path of the rate meal API.
     */
    public static final String PATH_RATING = "rating";
    /**
     * Path of the merge meal API.
     */
    public static final String PATH_MERGE = "merge";
    /**
     * Path of the names getter API.
     */
    public static final String PATH_NAMES = "names";
    /**
     * Path of the upload image API.
     */
    public static final String PATH_IMAGE = "image";

    // Strings for JSON
    public static final String API_MEAL_DATA = "data";
    public static final String API_MEAL_NAME = "name";
    public static final String API_MEAL_TAG = "tags";
    public static final String API_MEAL_RATINGS = "ratings";
    public static final String API_GLOBAL_RATING = "average";
    public static final String API_USER_RATING = "currentUserRating";
    public static final String API_TAG_BIO = "bio";
    public static final String API_TAG_FISH = "fish";
    public static final String API_TAG_PORK = "pork";
    public static final String API_TAG_COW = "cow";
    public static final String API_TAG_COW_AW = "cow_aw";
    public static final String API_TAG_VEGAN = "vegan";
    public static final String API_TAG_VEG = "veg";
    public static final String API_MEAL_INGREDIENTS = "add";
    public static final String API_MEAL_IMAGES = "images";
    public static final String API_MEAL_ID = "mealid";
    public static final String API_USER_ID = "token";
    public static final String API_RATING_VALUE = "value";
    public static final String API_FIRST_MEAL_ID = "mealid1";
    public static final String API_SECOND_MEAL_ID = "mealid2";
    public static final String API_MEAL = "meal";
    public static final String API_LINE = "line";
    public static final String API_PRICES = "price";
    public static final String API_PRICE_STUDENTS = "studentPrice";
    public static final String API_PRICE_GUESTS = "visitorPrice";
    public static final String API_PRICE_STAFF = "workerPrice";
    public static final String API_PRICE_PUPILS = "childPrice";
    public static final String API_DATE = "timestamp";
    public static final String API_IMAGE =  "image";

    /**
     * Contruct URL for requests to the server with the give path.
     * @param path path of the API
     * @return the url of the API
     * @throws MalformedURLException
     */
    public static URL getURL(String path) throws MalformedURLException {
        Uri.Builder builtUri = Uri.parse(BASE_URL).buildUpon();
        switch (path) {
            case PATH_IMAGE: builtUri.appendPath(PATH_IMAGE); break;
            case PATH_MEAL: builtUri.appendPath(PATH_MEAL); break;
            case PATH_MERGE: builtUri.appendPath(PATH_MERGE); break;
            case PATH_NAMES: builtUri.appendPath(PATH_NAMES); break;
            case PATH_RATING: builtUri.appendPath(PATH_RATING); break;
            default: return null;
        }
        return new URL(builtUri.build().toString());
    }

    /**
     * Construct URL for the plan getter API
     * @param path the path of the API
     * @param timestamp1 start date of the requested plan
     * @param timestamp2 end date of the requested plan
     * @return URL of the API
     * @throws MalformedURLException
     */
    public static URL getURL(String path, long timestamp1, long timestamp2) throws MalformedURLException {
        if (!path.equals(PATH_PLAN)) {
            return getURL(path);
        }

        Uri builtUri = Uri.parse(ServerApiContract.BASE_URL).buildUpon()
                .appendPath(ServerApiContract.PATH_PLAN)
                .appendPath(Long.toString(timestamp1))
                .appendPath(Long.toString(timestamp2))
                .build();

        return new URL(builtUri.toString());
    }

}
