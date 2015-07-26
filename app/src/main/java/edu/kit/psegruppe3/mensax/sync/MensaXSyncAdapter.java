package edu.kit.psegruppe3.mensax.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Vector;

import edu.kit.psegruppe3.mensax.R;
import edu.kit.psegruppe3.mensax.ServerApiContract;
import edu.kit.psegruppe3.mensax.data.CanteenContract;

/**
 * The MensaSyncAdapter class loads data from the server periodically in the background and
 * stores it using the content provider. It loads the current menu of the canteen and a list
 * of meal names to enable the search function.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class MensaXSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = MensaXSyncAdapter.class.getSimpleName();

    /**
     * Interval at which to sync with the canteen data for older devices, in seconds.
     */
    public static final int SYNC_INTERVAL = 60 * 180; //60 seconds (1 minute) * 180 = 3 hours

    /**
     * Interval at which to sync with the canteen data for newer devices, in seconds.
     */
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public MensaXSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        getMealNames();
        getMenu();
    }

    /**
     * Loads a meal names list with mealids from the server and stores it with the content provider.
     */
    private void getMealNames() {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String mealNamesJsonStr = null;

        try {
            URL url = ServerApiContract.getURL(ServerApiContract.PATH_NAMES);
            // Create the request to the Server, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            mealNamesJsonStr = buffer.toString();
            getMealNamesFromJson(mealNamesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the meal names data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    /**
     * Loads a the menu for the next five day from the server and stores it with the content provider
     * and deletes the old menu data.
     */
    private void getMenu() {
        Calendar calendar = Calendar.getInstance();
        int numDays = (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) ? 4 : 6;

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String menuJsonStr = null;

        try {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long startDate = calendar.getTimeInMillis();
            calendar.add(Calendar.DATE, numDays);
            long endDate = calendar.getTimeInMillis();

            URL url = ServerApiContract.getURL(ServerApiContract.PATH_PLAN, startDate / 1000, endDate / 1000);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            menuJsonStr = buffer.toString();
            getMenuDataFromJson(menuJsonStr, endDate);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the menu data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    private void getMealNamesFromJson(String mealNamesJsonStr) throws JSONException {
        try {
            JSONArray list = new JSONArray(mealNamesJsonStr);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(list.length());
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                String name = item.getString(ServerApiContract.API_MEAL_NAME);
                int id = item.getInt(ServerApiContract.API_MEAL_ID);
                Cursor mealCursor = getContext().getContentResolver().query(
                        CanteenContract.MealEntry.CONTENT_URI,
                        new String[]{CanteenContract.MealEntry._ID},
                        CanteenContract.MealEntry.COLUMN_MEAL_NAME + " = ?",
                        new String[]{name},
                        null);
                if (!mealCursor.moveToFirst()) {
                    ContentValues mealValues = new ContentValues();
                    mealValues.put(CanteenContract.MealEntry.COLUMN_MEAL_NAME, name);
                    mealValues.put(CanteenContract.MealEntry.COLUMN_MEAL_ID, id);
                    cVVector.add(mealValues);
                }

                mealCursor.close();
            }

            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(CanteenContract.MealEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, cVVector.size() + " Meals Inserted");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void getMenuDataFromJson(String menuJsonStr, long endDate) throws JSONException {
        final int TRUE = 1;
        final int FALSE = 0;

        try {
            JSONArray menuJson = new JSONArray(menuJsonStr);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(menuJson.length());
            for (int i = 0; i < menuJson.length(); i++) {
                String mealName;
                String ingredients;
                String line;
                long mealKey;
                long date;
                double globalRating;
                int tagBio;
                int tagFish;
                int tagPork;
                int tagCow;
                int tagCowAw;
                int tagVegan;
                int tagVeg;
                int priceStudents;
                int priceStaff;
                int priceGuests;
                int pricePupils;

                JSONObject offer = menuJson.getJSONObject(i);
                JSONObject meal = offer.getJSONObject(ServerApiContract.API_MEAL);
                JSONObject data = meal.getJSONObject(ServerApiContract.API_MEAL_DATA);
                JSONObject prices = offer.getJSONObject(ServerApiContract.API_PRICES);
                JSONObject tags = data.getJSONObject(ServerApiContract.API_MEAL_TAG);
                JSONObject ratings = data.getJSONObject(ServerApiContract.API_MEAL_RATINGS);

                mealName = meal.getString(ServerApiContract.API_MEAL_NAME);
                globalRating = ratings.getDouble(ServerApiContract.API_GLOBAL_RATING);
                tagBio = (tags.getBoolean(ServerApiContract.API_TAG_BIO)) ? TRUE : FALSE;
                tagFish = (tags.getBoolean(ServerApiContract.API_TAG_FISH)) ? TRUE : FALSE;
                tagPork = (tags.getBoolean(ServerApiContract.API_TAG_PORK)) ? TRUE : FALSE;
                tagCow = (tags.getBoolean(ServerApiContract.API_TAG_COW)) ? TRUE : FALSE;
                tagCowAw = (tags.getBoolean(ServerApiContract.API_TAG_COW_AW)) ? TRUE : FALSE;
                tagVegan = (tags.getBoolean(ServerApiContract.API_TAG_VEGAN)) ? TRUE : FALSE;
                tagVeg = (tags.getBoolean(ServerApiContract.API_TAG_VEG)) ? TRUE : FALSE;
                ingredients = tags.getString(ServerApiContract.API_MEAL_INGREDIENTS);

                Cursor mealCursor = getContext().getContentResolver().query(
                        CanteenContract.MealEntry.CONTENT_URI,
                        new String[]{CanteenContract.MealEntry._ID},
                        CanteenContract.MealEntry.COLUMN_MEAL_NAME + " = ?",
                        new String[]{mealName},
                        null);

                if (!mealCursor.moveToFirst()) {
                    return;
                }
                int mealKeyIndex = mealCursor.getColumnIndex(CanteenContract.MealEntry._ID);
                mealKey = mealCursor.getLong(mealKeyIndex);
                mealCursor.close();

                line = offer.getString(ServerApiContract.API_LINE);
                date = offer.getLong(ServerApiContract.API_DATE) * 1000;
                priceStudents = (int) (prices.getDouble(ServerApiContract.API_PRICE_STUDENTS) * 100);
                priceGuests = (int) (prices.getDouble(ServerApiContract.API_PRICE_GUESTS) * 100);
                priceStaff = (int) (prices.getDouble(ServerApiContract.API_PRICE_STAFF) * 100);
                pricePupils = (int) (prices.getDouble(ServerApiContract.API_PRICE_PUPILS) * 100);

                ContentValues offerValues = new ContentValues();
                offerValues.put(CanteenContract.OfferEntry.COLUMN_DATE, date);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_MEAL_KEY, mealKey);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_LINE, line);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_PRICE_STUDENTS, priceStudents);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_PRICE_GUESTS, priceGuests);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_PRICE_STAFF, priceStaff);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_PRICE_PUPILS, pricePupils);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_GLOBAL_RATING, globalRating);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_BIO, tagBio);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_FISH, tagFish);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_PORK, tagPork);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_COW, tagCow);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_COW_AW, tagCowAw);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_VEGAN, tagVegan);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_TAG_VEG, tagVeg);
                offerValues.put(CanteenContract.OfferEntry.COLUMN_INGREDIENTS, ingredients);
                cVVector.add(offerValues);
            }

            int inserted = 0;
            int deleted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);


                deleted = getContext().getContentResolver().delete(CanteenContract.OfferEntry.CONTENT_URI,
                        CanteenContract.OfferEntry.COLUMN_DATE + " <= ?",
                        new String[]{Long.toString(endDate)});

                inserted =  getContext().getContentResolver().bulkInsert(CanteenContract.OfferEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "Sync Complete. " + inserted + " Offers Inserted and " + deleted + "Offers deleted");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        MensaXSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        syncImmediately(context);
    }

    /**
     * Initializes the syncadapter.
     * @param context The context used to initialize the sync adapter
     */
    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}