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
 * This class is the actual content provider. It manages acces to a structured set
 * of data and provides insert, query, delete and update functions.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class CanteenProvider extends ContentProvider {

    /**
     * The URI Matcher used by this content provider to determine the kind of request of
     * an URI.
     */
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private CanteenDbHelper mOpenHelper;


    static final int OFFER = 100;
    static final int OFFER_WITH_DATE = 101;
    static final int MEAL = 300;
    static final int MEAL_WITH_NAME = 301;

    private static final SQLiteQueryBuilder sOfferByDateQueryBuilder;
    private static final SQLiteQueryBuilder sMealByNameQueryBuilder;

    static{
        sOfferByDateQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //offer INNER JOIN meal ON offer.meal_key = meal._id
        sOfferByDateQueryBuilder.setTables(
                CanteenContract.OfferEntry.TABLE_NAME + " INNER JOIN " +
                        CanteenContract.MealEntry.TABLE_NAME +
                        " ON " + CanteenContract.OfferEntry.TABLE_NAME +
                        "." + CanteenContract.OfferEntry.COLUMN_MEAL_KEY +
                        " = " + CanteenContract.MealEntry.TABLE_NAME +
                        "." + CanteenContract.MealEntry._ID);
        sMealByNameQueryBuilder = new SQLiteQueryBuilder();

        sMealByNameQueryBuilder.setTables(
                CanteenContract.MealEntry.TABLE_NAME);
    }

    //meal.meal_name = ?
    private static final String sNameSelection =
            CanteenContract.MealEntry.TABLE_NAME +
                    "." + CanteenContract.MealEntry.COLUMN_MEAL_NAME + " = ? ";

    //offer.date = ?
    private static final String sDateSelection =
            CanteenContract.OfferEntry.TABLE_NAME +
                    "." + CanteenContract.OfferEntry.COLUMN_DATE + " = ? ";

    private Cursor getOfferByDate(Uri uri, String[] projection, String sortOrder) {
        long date = CanteenContract.OfferEntry.getDateFromUri(uri);

        String[] selectionArgs;
        String selection;


        selectionArgs = new String[]{Long.toString(date)};
        selection = sDateSelection;

        return sOfferByDateQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }


    private Cursor getMealByName(Uri uri, String[] projection, String sortOrder) {
        String mealName = CanteenContract.MealEntry.getNameFromUri(uri);

        return sMealByNameQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sNameSelection,
                new String[]{mealName},
                null,
                null,
                sortOrder
        );
    }


    /**
     * Builds the URI Matcher for the content provider.
     *
     * @return the URI Matcher
     */
    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(CanteenContract.CONTENT_AUTHORITY, CanteenContract.PATH_OFFER, OFFER);
        uriMatcher.addURI(CanteenContract.CONTENT_AUTHORITY, CanteenContract.PATH_OFFER + "/#", OFFER_WITH_DATE);
        uriMatcher.addURI(CanteenContract.CONTENT_AUTHORITY, CanteenContract.PATH_MEAL, MEAL);
        uriMatcher.addURI(CanteenContract.CONTENT_AUTHORITY, CanteenContract.PATH_MEAL + "/*", MEAL_WITH_NAME);

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
            // "offer/*"
            case OFFER_WITH_DATE: {
                retCursor = getOfferByDate(uri, projection, sortOrder);
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

            case MEAL_WITH_NAME: {
                retCursor = getMealByName(uri, projection, sortOrder);
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

            case OFFER_WITH_DATE:
                return  CanteenContract.OfferEntry.CONTENT_TYPE;
            case OFFER:
                return CanteenContract.OfferEntry.CONTENT_TYPE;
            case MEAL:
                return CanteenContract.MealEntry.CONTENT_TYPE;
            case MEAL_WITH_NAME:
                return CanteenContract.MealEntry.CONTENT_ITEM_TYPE;
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
        int returnCount = 0;
        switch (match) {
            case OFFER:
                db.beginTransaction();
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
            case MEAL:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CanteenContract.MealEntry.TABLE_NAME, null, value);
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


    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
