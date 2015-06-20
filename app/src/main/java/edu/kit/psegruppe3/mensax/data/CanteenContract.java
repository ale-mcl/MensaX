package edu.kit.psegruppe3.mensax.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the weather database.
 */
public class CanteenContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "edu.kit.psegruppe3.mensax";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_OFFER = "offer";
    public static final String PATH_MEAL = "meal";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /*
        Inner class that defines the table contents of the location table
        Students: This is where you will add the strings.  (Similar to what has been
        done for WeatherEntry)
     */
    public static final class OfferEntry implements BaseColumns {
        public static final String TABLE_NAME = "offer";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_MEAL_KEY = "meal_key";

        public static final String COLUMN_LINE = "line";

        public static final String COLUMN_PRICE_STUDENTS = "price_students";

        public static final String COLUMN_PRICE_GUESTS = "price_guests";

        public static final String COLUMN_PRICE_STAFF = "price_staff";

        public static final String COLUMN_PRICE_PUPILS = "price_pupils";

        public static final String COLUMN_GLOBAL_RATING = "global_rating";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_OFFER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OFFER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OFFER;

        public static Uri buildOfferUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildOfferDate(long date) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(normalizeDate(date))).build();
        }

        public static String getLineFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

    /*
        Inner class that defines the table contents of the location table
        Students: This is where you will add the strings.  (Similar to what has been
        done for WeatherEntry)
     */
    public static final class MealEntry implements BaseColumns {
        public static final String TABLE_NAME = "meal";

        public static final String COLUMN_MEAL_NAME = "meal_name";

        public static final String COLUMN_MEAL_ID = "meal_id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEAL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEAL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEAL;

        public static Uri buildMealUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMealWithMealNameUri(String mealName) {
            return CONTENT_URI.buildUpon().appendPath(mealName).build();
        }

        public static String getNameFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


}
