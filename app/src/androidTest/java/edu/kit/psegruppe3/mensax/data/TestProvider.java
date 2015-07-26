package edu.kit.psegruppe3.mensax.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import edu.kit.psegruppe3.mensax.data.CanteenContract.OfferEntry;
import edu.kit.psegruppe3.mensax.data.CanteenContract.MealEntry;

/**
 * TestProvider class.
 * @author MensaX-group
 * @version 1.0
 *
 * Note: This is not a complete set of tests of the MensaX ContentProvider, but it does test
 * that at least the basic functionality has been implemented correctly.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /**
     * Helper function that deletes all records from both database tables using the ContentProvider.
     * It also queries the ContentProvider to make sure that the database has been successfully
     * deleted, so it cannot be used until the Query and Delete functions have been written
     * in the ContentProvider.
     */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                OfferEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                MealEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                OfferEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Offer table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MealEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Meal table during delete", 0, cursor.getCount());
        cursor.close();
    }

    /**
     * Refactor this function to use the deleteAllRecordsFromProvider functionality.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    /**
     * We want each test to start with a clean slate.
     * Run deleteAllRecords in setUp (called by the test runner before each test).
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    /**
     * This test checks to make sure that the content provider is registered correctly.
     */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // Defined the component name based on the package name from the context and the
        // CanteenProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                CanteenProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Makes sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: CanteenProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + CanteenContract.CONTENT_AUTHORITY,
                    providerInfo.authority, CanteenContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // Provider isn't registered correctly.
            assertTrue("Error: CanteenProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /**
     * This test doesn't touch the database.
     * It verifies that the ContentProvider returns the correct type for each type of URI
     * that it can handle.
     */
    public void testGetType() {

        // content://edu.kit.psegruppe3.mensax/offer/
        String type = mContext.getContentResolver().getType(OfferEntry.CONTENT_URI);
        // vnd.android.cursor.dir/edu.kit.psegruppe3.mensax/offer/
        assertEquals("Error: the OfferEntry CONTENT_URI should return OfferEntry.CONTENT_TYPE",
                OfferEntry.CONTENT_TYPE, type);

        long testDate = 1419120000L; // December 21st, 2014

        // content://edu.kit.psegruppe3.mensax/offer/20140612
        type = mContext.getContentResolver().getType(
                OfferEntry.buildOfferDate(testDate));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/offer
        assertEquals("Error: the OfferEntry CONTENT_URI with date should return OfferEntry.CONTENT_TYPE",
                OfferEntry.CONTENT_TYPE, type);

        // content://edu.kit.psegruppe3.mensax/meal/
        type = mContext.getContentResolver().getType(MealEntry.CONTENT_URI);
        // vnd.android.cursor.dir/du.kit.psegruppe3.mensax/meal/
        assertEquals("Error: the MealEntry CONTENT_URI should return MealEntry.CONTENT_TYPE",
                MealEntry.CONTENT_TYPE, type);

        String testMeal = "pommes";
        // content://edu.kit.psegruppe3.mensax/meal/pommes
        type = mContext.getContentResolver().getType(MealEntry.buildMealWithMealNameUri(testMeal));
        // vnd.android.cursor.item/du.kit.psegruppe3.mensax/meal/pommes
        assertEquals("Error: the MealEntry CONTENT_URI with name should return MealEntry.CONTENT_ITEM_TYPE",
                MealEntry.CONTENT_ITEM_TYPE, type);

    }

    /**
     * This test uses the database directly to insert and then uses the ContentProvider to
     * read out the data.
     * See if the basic offer query functionality given in the ContentProvider is working correctly.
     */
    public void testBasicOfferQuery() {
        // insert our test records into the database
        CanteenDbHelper dbHelper = new CanteenDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMealCarbonara();
        long mealRowId = TestUtilities.insertMealCarbonara(mContext);

        // We have a meal, now add some offer
        ContentValues offerValues = TestUtilities.createOfferValues(mealRowId);

        long offerRowId = db.insert(OfferEntry.TABLE_NAME, null, offerValues);
        assertTrue("Unable to Insert OfferEntry into the Database", offerRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor offerCursor = mContext.getContentResolver().query(
                OfferEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicOfferQuery", offerCursor, offerValues);
    }

    /**
     * This test uses the database directly to insert and then uses the ContentProvider to
     * read out the data.
     * See if the meal queries are performing correctly.
     */
    public void testBasicMealQueries() {
        // insert our test records into the database
        CanteenDbHelper dbHelper = new CanteenDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMealCarbonara();
        long mealRowId = TestUtilities.insertMealCarbonara(mContext);

        // Test the basic content provider query
        Cursor mealCursor = mContext.getContentResolver().query(
                MealEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMealQueries, meal query", mealCursor, testValues);

        // Has the NotificationUri been set correctly?
        if ( Build.VERSION.SDK_INT >= 19 ) {
            assertEquals("Error: Meal Query did not properly set NotificationUri",
                    mealCursor.getNotificationUri(), MealEntry.CONTENT_URI);
        }
    }

    /**
     * This test uses the provider to insert and then update the data.
     * See if the update meal is functioning correctly.
     */
    public void testUpdateMeal() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createMealCarbonara();

        Uri mealUri = mContext.getContentResolver().
                insert(MealEntry.CONTENT_URI, values);
        long mealRowId = ContentUris.parseId(mealUri);

        // Verify we got a row back.
        assertTrue(mealRowId != -1);
        Log.d(LOG_TAG, "New row id: " + mealRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(MealEntry._ID, mealRowId);
        updatedValues.put(MealEntry.COLUMN_MEAL_NAME, "Pasta Carbonara");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor mealCursor = mContext.getContentResolver()
                .query(MealEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mealCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MealEntry.CONTENT_URI, updatedValues, MealEntry._ID + "= ?",
                new String[] { Long.toString(mealRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, throws an assertion.
        //
        // If the code is failing here, it means that the content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        mealCursor.unregisterContentObserver(tco);
        mealCursor.close();

        // A cursor is our primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MealEntry.CONTENT_URI,
                null,   // projection
                MealEntry._ID + " = " + mealRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateMeal.  Error validating meal entry update.",
                cursor, updatedValues);

        cursor.close();
    }

    // Make sure we can still delete after adding/updating stuff
    //
    // It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.

    /**
     * Make sure we can still delete after adding/updating stuff.
     *
     * Note: It relies on insertions with testInsertReadProvider, so insert and
     * query functionality must also be complete before this test can be used.
     */
    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createMealCarbonara();

        // Register a content observer for the insert, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MealEntry.CONTENT_URI, true, tco);
        Uri mealUri = mContext.getContentResolver().insert(MealEntry.CONTENT_URI, testValues);

        /**
         * See if the content observer is getting called.
         *
         * If this fails, insert meal isn't calling
         * getContext().getContentResolver().notifyChange(uri, null);
         */
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long mealRowId = ContentUris.parseId(mealUri);

        // Verify we got a row back.
        assertTrue(mealRowId != -1);

        // Data's inserted.
        // Now pull some out to stare at it and verify it made the round trip.

        // A cursor is the primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MealEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MealEntry.",
                cursor, testValues);

        // Now that we have a meal, we add some offers
        ContentValues offerValues = TestUtilities.createOfferValues(mealRowId);
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(OfferEntry.CONTENT_URI, true, tco);

        Uri offerInsertUri = mContext.getContentResolver()
                .insert(OfferEntry.CONTENT_URI, offerValues);
        assertTrue(offerInsertUri != null);

        /**
         * See if the content observer is getting called.
         *
         * If this fails, insert offer isn't calling
         * getContext().getContentResolver().notifyChange(uri, null);
         */
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor offerCursor = mContext.getContentResolver().query(
                OfferEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating OfferEntry insert.",
                offerCursor, offerValues);

        // Add the meal values in with the offer data to make sure
        // that the join worked and we actually get all the values back
        offerValues.putAll(testValues);

        // Get the Offer with date data
        offerCursor = mContext.getContentResolver().query(
                OfferEntry.buildOfferDate(TestUtilities.TEST_DATE),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating Offer Data by date.",
                offerCursor, offerValues);
    }

    /**
     * Makes sure we can still delete after adding/updating stuff
     *
     * Note: It relies on insertions with testInsertReadProvider, so insert and
     * query functionality must be complete before this test can be used.
     */
    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our meal delete.
        TestUtilities.TestContentObserver mealObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MealEntry.CONTENT_URI, true, mealObserver);

        // Register a content observer for our offer delete.
        TestUtilities.TestContentObserver offerObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(OfferEntry.CONTENT_URI, true, offerObserver);

        deleteAllRecordsFromProvider();

        // If either of these fail, most-likely because it's not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.
        // (only if the insertReadProvider is succeeding)
        mealObserver.waitForNotificationOrFail();
        offerObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(mealObserver);
        mContext.getContentResolver().unregisterContentObserver(offerObserver);
    }

    /**
     *
     */
    static private final int BULK_INSERT_RECORDS_TO_INSERT = 16;
    static ContentValues[] createBulkInsertOfferValues(long mealRowId) {
        long currentTestDate = TestUtilities.TEST_DATE;
        long millisecondsInADay = 1000*60*60*24;
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, currentTestDate+= millisecondsInADay ) {
            ContentValues offerValues = new ContentValues();
            offerValues.put(CanteenContract.OfferEntry.COLUMN_DATE, currentTestDate);
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
            returnContentValues[i] = offerValues;
        }
        return returnContentValues;
    }

    /**
     * This test will work with the built-in (default) provider implementation,
     * which just inserts records one-at-a-time, so really do implement the
     * BulkInsert ContentProvider function.
     */
    public void testBulkInsert() {
        // first, let's create a meal value
        ContentValues testValues = TestUtilities.createMealCarbonara();
        Uri mealUri = mContext.getContentResolver().insert(MealEntry.CONTENT_URI, testValues);
        long mealRowId = ContentUris.parseId(mealUri);

        // Verify we got a row back.
        assertTrue(mealRowId != -1);

        // Data's inserted.
        // Now pull some out to stare at it and verify it made the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MealEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testBulkInsert. Error validating MealEntry.",
                cursor, testValues);

        /**
         * Now we bulkInsert some offer.
         * We only implement BulkInsert for offer entries.
         */
        ContentValues[] bulkInsertContentValues = createBulkInsertOfferValues(mealRowId);

        // Register a content observer for bulk insert.
        TestUtilities.TestContentObserver offerObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(OfferEntry.CONTENT_URI, true, offerObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(OfferEntry.CONTENT_URI, bulkInsertContentValues);

        // If this fails, most-likely the
        // getContext().getContentResolver().notifyChange(uri, null);
        // in BulkInsert ContentProvider method isn't getting called.
        offerObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(offerObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is the primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                OfferEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                OfferEntry.COLUMN_DATE + " ASC"  // sort order == by DATE ASCENDING
        );

        // Should have as many records in the database as inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and should match the ones created.
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating OfferEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}

