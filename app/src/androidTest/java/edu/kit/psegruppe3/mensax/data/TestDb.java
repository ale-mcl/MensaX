package edu.kit.psegruppe3.mensax.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * TestDb class.
 * @author MensaX-group
 * @version 1.0
 *
 * The Database is going to be tested.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    /**
     * We want each test to start with a clean slate
     */
    void deleteTheDatabase() {

        mContext.deleteDatabase(CanteenDbHelper.DATABASE_NAME);
    }

    /**
     * This function gets called before each test is executed to delete the database.  This makes
     * sure that we always have a clean test.
     */
    public void setUp() {

        deleteTheDatabase();
    }

    /**
     * This only tests that the Meal table has the correct columns.
     * @throws Throwable if something wrong happens
     */
    public void testCreateDb() throws Throwable {

         // Build a HashSet of all of the table names we want to look for
         // There will be another table in the DB that stores the
         // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(CanteenContract.MealEntry.TABLE_NAME);
        tableNameHashSet.add(CanteenContract.OfferEntry.TABLE_NAME);

        mContext.deleteDatabase(CanteenDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new CanteenDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // check if the tables we want have been created
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the meal entry
        // and offer entry tables
        assertTrue("Error: Database created without both the meal entry and offer entry tables",
                tableNameHashSet.isEmpty());

        // do the tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + CanteenContract.MealEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> mealColumnHashSet = new HashSet<String>();
        mealColumnHashSet.add(CanteenContract.MealEntry._ID);
        mealColumnHashSet.add(CanteenContract.MealEntry.COLUMN_MEAL_NAME);
        mealColumnHashSet.add(CanteenContract.MealEntry.COLUMN_MEAL_ID);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            mealColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that the database doesn't contain all of the required meal
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required meal entry columns",
                mealColumnHashSet.isEmpty());
        db.close();
    }

    /**
     * Code to test that we can insert and query the meal database.
     * Look in TestUtilities under "createMealCarbonara" function.
     * Can also make use of the ValidateCurrentRecord function from within TestUtilities.
     */
    public void testMealTable() {

        insertMeal();
    }

    /**
     * Code to test that we can insert and query the offer database.
     * Look in TestUtilities  "createOfferValues" function.
     * Can also make use of the ValidateCurrentRecord function from within TestUtilities.
     */
    public void testOfferTable() {
        /** First insert the meal, and then use the mealRowId to insert
         * the offer. Make sure to cover as many failure cases as you can.
         *
         * Instead of rewriting all of the code we've already written in testMealTable
         * we move this code to insertMeal and then call insertMeal from both tests,
         * because we need the code to return the ID of the inserted meal
         * and the testMealTable can only return void because it's a test.
         */

        long mealRowId = insertMeal();

        // Make sure we have a valid row ID.
        assertFalse("Error: Meal Not Inserted Correctly", mealRowId == -1L);

        // First step: Get reference to writable database
        // If there's an error in the SQL table creation Strings,
        // errors will be thrown here when we try to get a writable database.
        CanteenDbHelper dbHelper = new CanteenDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step (Offer): Create offer values
        ContentValues offerValues = TestUtilities.createOfferValues(mealRowId);

        // Third Step (Offer): Insert ContentValues into database and get a row ID back
        long offerRowId = db.insert(CanteenContract.OfferEntry.TABLE_NAME, null, offerValues);
        assertTrue(offerRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is our primary interface to the query results.
        Cursor offerCursor = db.query(
                CanteenContract.OfferEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from meal query", offerCursor.moveToFirst() );

        // Fifth Step: Validate the location Query
        TestUtilities.validateCurrentRecord("testInsertReadDb offerEntry failed to validate",
                offerCursor, offerValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from offer query",
                offerCursor.moveToNext() );

        // Sixth Step: Close cursor and database
        offerCursor.close();
        dbHelper.close();
    }

    /**
     * This is a helper method.
     * Moved the code from testMealTable here so that we can call
     * this code from both testOfferTable and testMealTable
     * @return mealRowId
     */
    public long insertMeal() {
        // First step: Get reference to writable database
        // If there's an error in the SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        CanteenDbHelper dbHelper = new CanteenDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what we want to insert
        // (used the createMealCarbonara)
        ContentValues testValues = TestUtilities.createMealCarbonara();

        // Third Step: Insert ContentValues into database and get a row ID back
        long mealRowId;
        mealRowId = db.insert(CanteenContract.MealEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(mealRowId != -1);

        // Data's inserted.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is our primary interface to the query results.
        Cursor cursor = db.query(
                CanteenContract.MealEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue("Error: No Records returned from meal query", cursor.moveToFirst());

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (used the validateCurrentRecord function in TestUtilities to validate the query)
        TestUtilities.validateCurrentRecord("Error: Meal Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from meal query",
                cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return mealRowId;
    }
}
