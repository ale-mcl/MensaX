package edu.kit.psegruppe3.mensax.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import edu.kit.psegruppe3.mensax.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/*
    These are functions and some test data to make it easier to test the database and
    Content Provider.
 */
public class TestUtilities extends AndroidTestCase {
    static final String TEST_MEAL = "Carbonara";
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Use this to create some default weather values for the database tests.
     */
    static ContentValues createOfferValues(long mealRowId) {
        ContentValues offerValues = new ContentValues();
        offerValues.put(CanteenContract.OfferEntry.COLUMN_DATE, TEST_DATE);
        offerValues.put(CanteenContract.OfferEntry.COLUMN_MEAL_KEY, mealRowId);
        offerValues.put(CanteenContract.OfferEntry.COLUMN_LINE, 1);
        offerValues.put(CanteenContract.OfferEntry.COLUMN_PRICE_STUDENTS, 1.5);
        offerValues.put(CanteenContract.OfferEntry.COLUMN_PRICE_GUESTS, 2.4);
        offerValues.put(CanteenContract.OfferEntry.COLUMN_PRICE_STAFF, 1.4);
        offerValues.put(CanteenContract.OfferEntry.COLUMN_PRICE_PUPILS, 5.5);
        offerValues.put(CanteenContract.OfferEntry.COLUMN_GLOBAL_RATING, "rating");
        offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_BIO, "bio");
        offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_FISH, "fish");
        offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_PORK, "pork");
        offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_COW, "cow");
        offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_COW_AW, "cow_aw");
        offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_VEGAN, "vegan");
        offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_VEG, "veg");
        offerValues.put(CanteenContract.OfferEntry.COLUMN_INGREDIENTS, "everything");

        return offerValues;
    }

    /*
        MealEntry part of the CanteenContract.
    */
    static ContentValues createMealCarbonara() {
        // Create a new map of values, where column names are the keys //ID??
        ContentValues testValues = new ContentValues();
        testValues.put(CanteenContract.MealEntry.COLUMN_MEAL_NAME, TEST_MEAL);
        testValues.put(CanteenContract.MealEntry.COLUMN_MEAL_ID, 1);

        return testValues;
    }

    /*
        Creates the MealEntry part of the CanteenContract as well as the CanteenDbHelper.
     */
    static long insertMealCarbonara(Context context) {
        // insert our test records into the database
        CanteenDbHelper dbHelper = new CanteenDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMealCarbonara();

        long mealRowId;
        mealRowId = db.insert(CanteenContract.MealEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Carbonara Meal Values", mealRowId != -1);

        return mealRowId;
    }

    /*
        The functions provided inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
