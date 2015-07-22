package edu.kit.psegruppe3.mensax;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.kit.psegruppe3.mensax.datamodels.Meal;

/**
 * Fragment of the activity DetailActivity that shows all info about a meal.
 */
public class DetailFragment extends Fragment {

    private Meal meal;

    private TextView txtMealName;
    private TextView txtIngredients;
    private RatingBar globalRating;
    private RatingBar userRating;


    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        Integer[] mealId = {getArguments().getInt(DetailActivity.ARG_MEAL_ID)};

        txtMealName = (TextView) rootView.findViewById(R.id.mealName);

        TextView txtMealIngredients = (TextView) rootView.findViewById(R.id.mealIngredients);
        txtMealIngredients.setText(R.string.ingredients);

        txtIngredients = (TextView) rootView.findViewById(R.id.showIngredients);

        TextView txtMealGlobalRating = (TextView) rootView.findViewById(R.id.mealGlobalRating);
        txtMealGlobalRating.setText(R.string.global_rating);

        globalRating = (RatingBar) rootView.findViewById(R.id.showGlobalRating);

        TextView txtMealUserRating = (TextView) rootView.findViewById(R.id.mealUserRating);
        txtMealUserRating.setText(R.string.user_rating);

        userRating = (RatingBar) rootView.findViewById(R.id.showUserRating);

        FetchMealDataTask fetchMealDataTask = new FetchMealDataTask();
        fetchMealDataTask.execute(mealId);

        Button btnGiveRating = (Button) rootView.findViewById(R.id.button_giveRating);
        btnGiveRating.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder rankDialog;
                rankDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_give_rating, null);
                rankDialog.setView(dialogView);
                rankDialog.setCancelable(true);
                rankDialog.setTitle(R.string.dialog_giveRating);
                final RatingBar ratingBar = (RatingBar)dialogView.findViewById(R.id.dialog_ratingbar);
                rankDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        meal.setUserRating(ratingBar.getRating());
                    }
                });
                rankDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                rankDialog.show();
            }
        });

        return rootView;
    }

    private void uploadImage(Uri imageUri) {
    }

    private Uri selectImageUri() {
        return null;
    }

    private Meal selectMeal() {
        return null;
    }

    private void updateScreen() {
        if (meal == null) {
            return;
        }
        txtMealName.setText(meal.getName());

        txtIngredients.setText("[" + meal.getIngredients() + "]");

        globalRating.setRating(meal.getGlobalRating());

        userRating.setRating(meal.getUserRating());
    }

    private class FetchMealDataTask extends AsyncTask<Integer, Void, Meal> {

        private final String LOG_TAG = FetchMealDataTask.class.getSimpleName();

        @Override
        protected Meal doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String mealDataJsonStr = null;
            try {
                final String MEAL_DATA_BASE_URL =
                        "https://i43pc164.ipd.kit.edu/PSESoSe15Gruppe3/mensa/api/meal";

                Uri builtUri = Uri.parse(MEAL_DATA_BASE_URL).buildUpon()
                        .appendPath(Integer.toString(params[0]))
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
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
                    return null;
                }
                mealDataJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
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
            return getMealDataFromJson(mealDataJsonStr);
        }

        @Override
        protected void onPostExecute(Meal m) {
            meal = m;
            updateScreen();
            super.onPostExecute(m);
        }

        private Meal getMealDataFromJson(String jsonStr) {
            final String API_MEAL_DATA = "data";
            final String API_MEAL_NAME = "name";
            final String API_MEAL_ID = "id";
            final String API_MEAL_TAG = "tags";
            final String API_TAG_BIO = "bio";
            final String API_TAG_FISH = "fish";
            final String API_TAG_PORK = "pork";
            final String API_TAG_COW = "cow";
            final String API_TAG_COW_AW = "cow_aw";
            final String API_TAG_VEGAN = "vegan";
            final String API_TAG_VEG = "veg";
            final String API_MEAL_INGREDIENTS = "add";
            final String API_MEAL_IMAGES = "images";

            Meal newMeal = null;

            try {
                JSONObject meal = new JSONObject(jsonStr);
                JSONObject data = meal.getJSONObject(API_MEAL_DATA);
                JSONObject tags = data.getJSONObject(API_MEAL_TAG);
                JSONArray images = data.getJSONArray(API_MEAL_IMAGES);

                String mealName = meal.getString(API_MEAL_NAME);
                String ingredients = tags.getString(API_MEAL_INGREDIENTS);
                int mealId = meal.getInt(API_MEAL_ID);
                boolean tagBio = tags.getBoolean(API_TAG_BIO);
                boolean tagFish = tags.getBoolean(API_TAG_FISH);
                boolean tagPork = tags.getBoolean(API_TAG_PORK);
                boolean tagCow = tags.getBoolean(API_TAG_COW);
                boolean tagCowAw = tags.getBoolean(API_TAG_COW_AW);
                boolean tagVegan = tags.getBoolean(API_TAG_VEGAN);
                boolean tagVeg = tags.getBoolean(API_TAG_VEG);

                newMeal = new Meal(mealName, mealId);
                newMeal.setTag(Meal.TAG_BIO, tagBio);
                newMeal.setTag(Meal.TAG_FISH, tagFish);
                newMeal.setTag(Meal.TAG_PORK, tagPork);
                newMeal.setTag(Meal.TAG_COW, tagCow);
                newMeal.setTag(Meal.TAG_COW_AW, tagCowAw);
                newMeal.setTag(Meal.TAG_VEGAN, tagVegan);
                newMeal.setTag(Meal.TAG_VEG, tagVeg);
                newMeal.setIngredients(ingredients);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return newMeal;
        }
    }
}