package edu.kit.psegruppe3.mensax.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the weather database.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class CanteenContract {

    /**
     * The name of the entire content provider
     */
    public static final String CONTENT_AUTHORITY = "edu.kit.psegruppe3.mensax";

    /**
     * The base of all URI's which apps will use to contact the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path for offer data.
     */
    public static final String PATH_OFFER = "offer";

    /**
     * Path for meal data.
     */
    public static final String PATH_MEAL = "meal";

    /**
     * Calculates the start of the Julian day of the given date
     * @param startDate the given date
     * @return start of the julian day of the given date
     */
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /**
     * Inner class that defines the table contents of the offer table
     */
    public static final class OfferEntry implements BaseColumns {
        /**
         * The name of the offer table.
         */
        public static final String TABLE_NAME = "offer";

        /**
         * The date at which the offer will be offered.
         */
        public static final String COLUMN_DATE = "date";

        /**
         * Column with the foreign key into the meal table.
         */
        public static final String COLUMN_MEAL_KEY = "meal_key";

        /**
         * The line in which the offer is offered.
         */
        public static final String COLUMN_LINE = "line";

        /**
         * Price for students.
         */
        public static final String COLUMN_PRICE_STUDENTS = "price_students";

        /**
         * Price for guests.
         */
        public static final String COLUMN_PRICE_GUESTS = "price_guests";

        /**
         * Price for the KIT staff
         */
        public static final String COLUMN_PRICE_STAFF = "price_staff";

        /**
         * Price for pupils.
         */
        public static final String COLUMN_PRICE_PUPILS = "price_pupils";

        /**
         * The average rating of the meal of the offer.
         */
        public static final String COLUMN_GLOBAL_RATING = "global_rating";

        /**
         * Determines whether the offer has the bio tag.
         */
        public static final String COLUMN_TAG_BIO = "bio";

        /**
         * Determines whether the offer contains fish.
         */
        public static final String COLUMN_TAG_FISH = "fish";

        /**
         * Determines whether the offer contains pork.
         */
        public static final String COLUMN_TAG_PORK = "pork";

        /**
         * Determines whether the offer contains beef.
         */
        public static final String COLUMN_TAG_COW = "cow";

        /**
         * Determines whether the offer contains beef from species-appropriate husbandry.
         */
        public static final String COLUMN_TAG_COW_AW = "cow_aw";

        /**
         * Determines whether the offer is vegan.
         */
        public static final String COLUMN_TAG_VEGAN = "vegan";

        /**
         * Determines whether the offer is vegetarian.
         */
        public static final String COLUMN_TAG_VEG = "veg";

        /**
         * The additives which the offer contains.
         */
        public static final String COLUMN_INGREDIENTS = "ingredients";

        /**
         * The base URI for the offer table.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_OFFER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OFFER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OFFER;

        /**
         * Helper method to build an URI to request a specified row of the offer table.
         *
         * @param id id of the row
         * @return the URI to request a specified row of the offer table
         */
        public static Uri buildOfferUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Helper method to build an URI to request offers which will be offered
         * at the specified date.
         *
         * @param date the specified date
         * @return the URI to request offers which will be offered at the specified date
         */
        public static Uri buildOfferDate(long date) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(normalizeDate(date))).build();
        }

        /**
         * Return the date parameter of a given URI.
         *
         * @param uri URI to extract the date parameter from
         * @return the date parameter of the given URI
         */
        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

    /**
     * Inner class that defines the table contents of the meal table.
     */
    public static final class MealEntry implements BaseColumns {
        /**
         * The name of the meal table.
         */
        public static final String TABLE_NAME = "meal";

        /**
         * The name of the meal.
         */
        public static final String COLUMN_MEAL_NAME = "meal_name";

        /**
         * The id of the meal at the server.
         */
        public static final String COLUMN_MEAL_ID = "meal_id";

        /**
         * The base URI for the meal table.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEAL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEAL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEAL;

        /**
         * Helper method to build an URI to request a specified row of the meal table.
         *
         * @param id id of the row
         * @return the URI to request a specified row of the meal table
         */
        public static Uri buildMealUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Helper method to build an URI to request meals with the given name
         *
         * @param mealName the given name
         * @return the URI to request meals with the given name
         */
        public static Uri buildMealWithMealNameUri(String mealName) {
            return CONTENT_URI.buildUpon().appendPath(mealName).build();
        }

        public static String getNameFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


}
