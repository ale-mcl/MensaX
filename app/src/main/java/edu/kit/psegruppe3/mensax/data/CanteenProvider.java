package edu.kit.psegruppe3.mensax.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by ekremsenturk on 16.06.15.
 */
public class CanteenProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CanteenDbHelper mOpenHelper;

    static final int OFFER = 100;
    static final int OFFER_WITH_LINE = 101;
    static final int OFFER_WITH_LINE_AND_DATE = 102;
    static final int MEAL = 300;

    private static final SQLiteQueryBuilder sOfferByLineQueryBuilder;

    static{
        sOfferByLineQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //offer INNER JOIN meal ON offer.meal_key = meal._id
        sOfferByLineQueryBuilder.setTables(
                CanteenContract.OfferEntry.TABLE_NAME + " INNER JOIN " +
                        CanteenContract.MealEntry.TABLE_NAME +
                        " ON " + CanteenContract.OfferEntry.TABLE_NAME +
                        "." + CanteenContract.OfferEntry.COLUMN_MEAL_KEY +
                        " = " + CanteenContract.MealEntry.TABLE_NAME +
                        "." + CanteenContract.MealEntry._ID);
    }

    //offer.line = ?
    private static final String sLineSelection =
            CanteenContract.OfferEntry.TABLE_NAME+
                    "." + CanteenContract.OfferEntry.COLUMN_LINE + " = ? ";

    //offer.line = ? AND date >= ?
    private static final String sLineWithStartDateSelection =
            CanteenContract.OfferEntry.TABLE_NAME+
                    "." + CanteenContract.OfferEntry.COLUMN_LINE + " = ? AND " +
                    CanteenContract.OfferEntry.COLUMN_DATE + " >= ? ";

    //offer.line = ? AND date = ?
    private static final String sLineAndDaySelection =
            CanteenContract.OfferEntry.TABLE_NAME +
                    "." + CanteenContract.OfferEntry.COLUMN_LINE + " = ? AND " +
                    CanteenContract.OfferEntry.COLUMN_DATE + " = ? ";

    private Cursor getOfferByLine(Uri uri, String[] projection, String sortOrder) {
        String line = CanteenContract.OfferEntry.getLineFromUri(uri);
        long startDate = CanteenContract.OfferEntry.getStartDateFromUri(uri);

        String[] selectionArgs;
        String selection;

        if (startDate == 0) {
            selection = sLineSelection;
            selectionArgs = new String[]{line};
        } else {
            selectionArgs = new String[]{line, Long.toString(startDate)};
            selection = sLineWithStartDateSelection;
        }

        return sOfferByLineQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getOfferByLineAndDate(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = CanteenContract.OfferEntry.getLineFromUri(uri);
        long date = CanteenContract.OfferEntry.getDateFromUri(uri);

        return sOfferByLineQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sLineAndDaySelection,
                new String[]{locationSetting, Long.toString(date)},
                null,
                null,
                sortOrder
        );
    }

    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the WEATHER, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
        and LOCATION integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.
        uriMatcher.addURI(CanteenContract.CONTENT_AUTHORITY, CanteenContract.PATH_OFFER, OFFER);
        uriMatcher.addURI(CanteenContract.CONTENT_AUTHORITY, CanteenContract.PATH_OFFER + "/*", OFFER_WITH_LINE);
        uriMatcher.addURI(CanteenContract.CONTENT_AUTHORITY, CanteenContract.PATH_OFFER + "/*/#", OFFER_WITH_LINE_AND_DATE);
        uriMatcher.addURI(CanteenContract.CONTENT_AUTHORITY, CanteenContract.PATH_MEAL, MEAL);


        // 3) Return the new matcher!
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new CanteenDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "offer/*/#"
            case OFFER_WITH_LINE_AND_DATE:
            {
                retCursor = getOfferByLineAndDate(uri, projection, sortOrder);
                break;
            }
            // "offer/*"
            case OFFER_WITH_LINE: {
                retCursor = getOfferByLine(uri, projection, sortOrder);
                break;
            }
            // "offer"
            case OFFER: {
                retCursor = mOpenHelper.getReadableDatabase().query(CanteenContract.OfferEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            // "meal"
            case MEAL: {
                retCursor = mOpenHelper.getReadableDatabase().query(CanteenContract.MealEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case OFFER_WITH_LINE_AND_DATE:
                return CanteenContract.OfferEntry.CONTENT_ITEM_TYPE;
            case OFFER_WITH_LINE:
                return  CanteenContract.OfferEntry.CONTENT_TYPE;
            case OFFER:
                return CanteenContract.OfferEntry.CONTENT_TYPE;
            case MEAL:
                return CanteenContract.MealEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case OFFER: {
                normalizeDate(values);
                long _id = db.insert(CanteenContract.OfferEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = CanteenContract.OfferEntry.buildOfferUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MEAL: {
                normalizeDate(values);
                long _id = db.insert(CanteenContract.MealEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CanteenContract.MealEntry.buildMealUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection) selection = "1";
        switch (match) {
            case OFFER: {
                rowsDeleted = db.delete(CanteenContract.OfferEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MEAL: {
                rowsDeleted = db.delete(CanteenContract.MealEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsImpacted;

        switch (match) {
            case OFFER: {
                rowsImpacted = db.update(CanteenContract.OfferEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case MEAL: {
                rowsImpacted = db.update(CanteenContract.MealEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsImpacted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsImpacted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case OFFER:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(CanteenContract.OfferEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(CanteenContract.OfferEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(CanteenContract.OfferEntry.COLUMN_DATE);
            values.put(CanteenContract.OfferEntry.COLUMN_DATE, CanteenContract.normalizeDate(dateValue));
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
