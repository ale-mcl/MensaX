package edu.kit.psegruppe3.mensax.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.kit.psegruppe3.mensax.data.CanteenContract.MealEntry;
import edu.kit.psegruppe3.mensax.data.CanteenContract.OfferEntry;

/**
 * Created by ekremsenturk on 16.06.15.
 */
public class CanteenDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "canteen.db";

    public CanteenDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_OFFER_TABLE = "CREATE TABLE " + OfferEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                OfferEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                OfferEntry.COLUMN_MEAL_KEY + " INTEGER NOT NULL, " +
                OfferEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                OfferEntry.COLUMN_LINE + " TEXT NOT NULL, " +
                OfferEntry.COLUMN_PRICE + " INTEGER NOT NULL," +
                OfferEntry.COLUMN_RATING + " INTEGER NOT NULL," +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + OfferEntry.COLUMN_MEAL_KEY + ") REFERENCES " +
                MealEntry.TABLE_NAME + " (" + MealEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + OfferEntry.COLUMN_LINE + ", " +
                OfferEntry.COLUMN_MEAL_KEY + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_OFFER_TABLE);

        final String SQL_CREATE_MEAL_TABLE = "CREATE TABLE " + MealEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                MealEntry._ID + " INTEGER PRIMARY KEY," +


                MealEntry.COLUMN_MEAL_NAME + " TEXT UNIQUE NOT NULL, " +
                MealEntry.COLUMN_MEAL_ID + " INTEGER NOT NULL " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MEAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OfferEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MealEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
