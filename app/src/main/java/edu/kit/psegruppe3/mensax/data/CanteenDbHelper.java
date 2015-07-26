package edu.kit.psegruppe3.mensax.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.kit.psegruppe3.mensax.data.CanteenContract.MealEntry;
import edu.kit.psegruppe3.mensax.data.CanteenContract.OfferEntry;

/**
 * Manages a local database for the meal and offer data.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class CanteenDbHelper extends SQLiteOpenHelper {

    /**
     * The version of the db.
     */
    private static final int DATABASE_VERSION = 2;

    /**
     * The name of the db.
     */
    static final String DATABASE_NAME = "canteen.db";

    /**
     * Standard constructor for this class
     *
     * @param context the context in which this dbhelper is used
     */
    public CanteenDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_OFFER_TABLE = "CREATE TABLE " + OfferEntry.TABLE_NAME + " (" +
                OfferEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the offer entry associated with this offer data
                OfferEntry.COLUMN_MEAL_KEY + " INTEGER NOT NULL, " +
                OfferEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                OfferEntry.COLUMN_LINE + " TEXT NOT NULL, " +
                OfferEntry.COLUMN_PRICE_STUDENTS + " INTEGER NOT NULL," +
                OfferEntry.COLUMN_PRICE_GUESTS + " INTEGER NOT NULL," +
                OfferEntry.COLUMN_PRICE_STAFF + " INTEGER NOT NULL," +
                OfferEntry.COLUMN_PRICE_PUPILS + " INTEGER NOT NULL," +
                OfferEntry.COLUMN_GLOBAL_RATING + " REAL NOT NULL," +
                OfferEntry.COLUMN_TAG_BIO + " INTEGER NOT NULL," +
                OfferEntry.COLUMN_TAG_FISH + " INTEGER NOT NULL," +
                OfferEntry.COLUMN_TAG_PORK + " INTEGER NOT NULL," +
                OfferEntry.COLUMN_TAG_COW + " INTEGER NOT NULL," +
                OfferEntry.COLUMN_TAG_COW_AW + " INTEGER NOT NULL," +
                OfferEntry.COLUMN_TAG_VEGAN + " INTEGER NOT NULL," +
                OfferEntry.COLUMN_TAG_VEG + " INTEGER NOT NULL," +
                OfferEntry.COLUMN_INGREDIENTS + " TEXT NOT NULL," +

                // Set up the meal column as a foreign key to meal table.
                " FOREIGN KEY (" + OfferEntry.COLUMN_MEAL_KEY + ") REFERENCES " +
                MealEntry.TABLE_NAME + " (" + MealEntry._ID + "), " +

                // To assure the application have just one offer entry per day
                // per line, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + OfferEntry.COLUMN_DATE + ", " +
                OfferEntry.COLUMN_LINE + ", " +
                OfferEntry.COLUMN_MEAL_KEY + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_OFFER_TABLE);

        final String SQL_CREATE_MEAL_TABLE = "CREATE TABLE " + MealEntry.TABLE_NAME + " (" +
                // the ID of the meal entry associated with this meal data
                MealEntry._ID + " INTEGER PRIMARY KEY," +


                MealEntry.COLUMN_MEAL_NAME + " TEXT UNIQUE NOT NULL, " +
                MealEntry.COLUMN_MEAL_ID + " INTEGER NOT NULL, " +
                // To assure the application does not have two or more meal entries
                // with the same name, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + MealEntry.COLUMN_MEAL_NAME + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MEAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OfferEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MealEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
