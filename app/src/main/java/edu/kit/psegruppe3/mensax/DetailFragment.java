package edu.kit.psegruppe3.mensax;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.kit.psegruppe3.mensax.datamodels.Meal;

/**
 * Fragment of the activity DetailActivity that shows all info about a meal.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class DetailFragment extends Fragment {

    /**
     * Request code to capture a picture with a camera.
     */
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int IMAGE_WIDTH = 1080;
    private static final int IMAGE_HEIGHT = 720;

    private final String LOG_TAG = DetailFragment.class.getSimpleName();

    private Meal meal;
    private TextView txtMealName;
    private TextView txtIngredients;
    private RatingBar globalRating;
    private RatingBar userRating;
    private Gallery gallery;
    private TextView firstTagTextView;
    private TextView secondTagTextView;

    /**
     * Constructor of the class.
     */
    public DetailFragment() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        Integer[] mealId = {getArguments().getInt(DetailActivity.ARG_MEAL_ID)};

        gallery = (Gallery) rootView.findViewById(R.id.gallery1);
        txtMealName = (TextView) rootView.findViewById(R.id.mealName);
        txtIngredients = (TextView) rootView.findViewById(R.id.showIngredients);
        globalRating = (RatingBar) rootView.findViewById(R.id.showGlobalRating);
        userRating = (RatingBar) rootView.findViewById(R.id.showUserRating);
        firstTagTextView = (TextView) rootView.findViewById(R.id.first_tag_textview);
        secondTagTextView = (TextView) rootView.findViewById(R.id.second_tag_textview);

        FetchMealDataTask fetchMealDataTask = new FetchMealDataTask();
        fetchMealDataTask.execute(mealId);

        Button btnGiveRating = (Button) rootView.findViewById(R.id.button_giveRating);
        btnGiveRating.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (meal != null) {
                    AlertDialog.Builder rankDialog;
                    rankDialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_give_rating, null);
                    rankDialog.setView(dialogView);
                    rankDialog.setCancelable(true);
                    rankDialog.setTitle(R.string.dialog_giveRating);
                    final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.dialog_ratingbar);
                    rankDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int newRating = (int) ratingBar.getRating();
                            RateMealTask rateMealTask = new RateMealTask();
                            rateMealTask.execute(meal.getMealId(), newRating);
                        }
                    });
                    rankDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    rankDialog.show();
                }
            }
        });

        Button btnTakePicture = (Button) rootView.findViewById(R.id.button_takePicture);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (meal != null) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        Button btnMergeMeal = (Button) rootView.findViewById(R.id.button_mergeMeal);
        btnMergeMeal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (meal != null) {
                    askSearchQuery();
                }
            }
        });
        return rootView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            String stringOfPhoto = bitmapToString(photo);
            UploadPictureTask uploadPictureTask = new UploadPictureTask();
            uploadPictureTask.execute(String.valueOf(meal.getMealId()), stringOfPhoto);

        } else if (requestCode == SearchableActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data.hasExtra(DetailActivity.ARG_MEAL_ID)) {
                int mealId = data.getIntExtra(DetailActivity.ARG_MEAL_ID, 0);
                MergeMealTask mergeMealTask = new MergeMealTask();
                mergeMealTask.execute(meal.getMealId(), mealId);
            }
        }
    }

    private String bitmapToString(Bitmap bitmap) {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte [] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void updateScreen(boolean downloadImages) {
        if (meal == null) {
            return;
        }
        if (downloadImages) {
            DownloadPictureTask downloadPictureTask = new DownloadPictureTask();
            downloadPictureTask.execute(meal.getImages());
        }

        int i = Meal.TAG_BIO;
        while (i <= Meal.TAG_VEG) {
            if (meal.hasTag(i)) {
                firstTagTextView.setText(Utility.getTagString(getActivity(), i));
                firstTagTextView.setCompoundDrawablesWithIntrinsicBounds(Utility.getTagDrawable(getActivity(), i), null, null, null);
                i++;
                break;
            }
            i++;
        }
        while (i <= Meal.TAG_VEG) {
            if (meal.hasTag(i)) {
                secondTagTextView.setText(Utility.getTagString(getActivity(), i));
                secondTagTextView.setCompoundDrawablesWithIntrinsicBounds(Utility.getTagDrawable(getActivity(), i), null, null, null);
                break;
            }
            i++;
        }

        txtMealName.setText(meal.getName());

        if (!meal.getIngredients().equals("[]")) { //no additives
            txtIngredients.setText(getString(R.string.ingredients, meal.getIngredients()));
        } else {
            txtIngredients.setText(getString(R.string.no_ingredients));
        }

        globalRating.setRating((float) meal.getGlobalRating());
        userRating.setRating((float) meal.getUserRating());
    }

    private String getToken() {
        final String scope = "audience:server:client_id:785844054287-7hge652kf27md81acog9vg1u0nk9so83.apps.googleusercontent.com";
        try {
            String email = getGoogleEmailAdress();
            return GoogleAuthUtil.getToken(getActivity(), email, scope);
        } catch (IOException e) {
            Log.d(LOG_TAG, "Error");
        } catch (UserRecoverableAuthException e ) {
            Log.d(LOG_TAG, "Error");
        } catch (GoogleAuthException e ) {
            Log.d(LOG_TAG, "Error");
        }
        return null;
    }

    private void sendSearchRequest(String query) {
        Intent mIntent = new Intent(getActivity(), SearchableActivity.class);
        mIntent.setAction(Intent.ACTION_SEARCH);
        mIntent.putExtra(SearchableActivity.ARG_SELECT_MEAL, true);
        mIntent.putExtra(SearchManager.QUERY, query);
        startActivityForResult(mIntent, SearchableActivity.REQUEST_CODE);
    }

    private void askSearchQuery() {
        final EditText input = new EditText(getActivity());
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(R.string.merge_dialog_title);
        adb.setMessage(R.string.merge_dialog_message);
        adb.setView(input);
        adb.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Editable upc = input.getText();
                sendSearchRequest(upc.toString());

                dialog.cancel();
            }
        });
        adb.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        adb.create().show();
    }

    private String getGoogleEmailAdress() {
        AccountManager accountManager = AccountManager.get(getActivity());
        Account[] accounts = accountManager.getAccountsByType("com.google");
        if (accounts.length > 0) {
            return accounts[0].name;
        } else {
            return null;
        }
    }

    private class UploadPictureTask extends AsyncTask<String, Void, Integer> {

        private final String LOG_TAG = UploadPictureTask.class.getSimpleName();

        @Override
        protected Integer doInBackground(String... params) {
            if (params.length < 2) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            OutputStreamWriter writer = null;
            int response = -1;
            try {
                URL url = ServerApiContract.getURL(ServerApiContract.PATH_IMAGE);

                String token = getToken();
                String output = getJsonString(params[0], token, params[1]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(output);
                writer.flush();
                response = urlConnection.getResponseCode();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return response;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return response;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return response;
        }

        private String getJsonString(String mealId, String userId, String image) throws JSONException {
            String imageJsonStr = "";

            JSONObject imageString = new JSONObject();
            imageString.put(ServerApiContract.API_MEAL_ID, mealId);
            imageString.put(ServerApiContract.API_USER_ID, userId);
            imageString.put(ServerApiContract.API_IMAGE, image);
            imageJsonStr = imageString.toString();

            return imageJsonStr;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(Integer response) {
            if (getActivity() != null) {
                if (response == HttpURLConnection.HTTP_OK) {
                    Toast.makeText(getActivity(), getString(R.string.toast_image_upload_succesfull), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_image_upload_failed), Toast.LENGTH_SHORT).show();
                }
            }
            super.onPostExecute(response);
        }
    }

    private class FetchMealDataTask extends AsyncTask<Integer, Void, Meal> {

        private final String LOG_TAG = FetchMealDataTask.class.getSimpleName();

        /**
         * {@inheritDoc}
         */
        @Override
        protected Meal doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }

            Meal newMeal = null;
            HttpURLConnection urlConnection = null;
            OutputStreamWriter writer = null;
            BufferedReader reader = null;

            try {
                URL url = ServerApiContract.getURL(ServerApiContract.PATH_MEAL);

                String token = getToken();
                String output = getJsonString(params[0], token);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(output);
                writer.flush();

                InputStream input = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                StringBuffer result = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                newMeal = getMealDataFromJson(result.toString());
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
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
            return newMeal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(Meal m) {
            if (getActivity() != null) {
                if (m != null) {
                    meal = m;
                    updateScreen(true);
                } else {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setCancelable(false);
                    adb.setTitle(R.string.error_dialog_title);
                    adb.setMessage(R.string.error_dialog_message);
                    adb.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });
                    adb.show();
                }
            }

            super.onPostExecute(m);
        }

        private Meal getMealDataFromJson(String jsonStr) throws JSONException {
            Meal newMeal = null;

            JSONObject meal = new JSONObject(jsonStr);
            JSONObject data = meal.getJSONObject(ServerApiContract.API_MEAL_DATA);
            JSONObject tags = data.getJSONObject(ServerApiContract.API_MEAL_TAG);
            JSONObject ratings = data.getJSONObject(ServerApiContract.API_MEAL_RATINGS);
            JSONArray images = data.getJSONArray(ServerApiContract.API_MEAL_IMAGES);

            String[] imageUris = new String[images.length()];
            for (int i = 0; i < images.length(); i++) {
                imageUris[i] = images.getString(i);
            }
            String mealName = meal.getString(ServerApiContract.API_MEAL_NAME);
            String ingredients = tags.getString(ServerApiContract.API_MEAL_INGREDIENTS);
            int mealId = meal.getInt(ServerApiContract.API_MEAL_ID);
            int userRating = 0;
            if (!ratings.get(ServerApiContract.API_USER_RATING).equals(null)) {
                userRating = ratings.getInt(ServerApiContract.API_USER_RATING);
            }
            double globalRating = ratings.getDouble(ServerApiContract.API_GLOBAL_RATING);
            boolean tagBio = tags.getBoolean(ServerApiContract.API_TAG_BIO);
            boolean tagFish = tags.getBoolean(ServerApiContract.API_TAG_FISH);
            boolean tagPork = tags.getBoolean(ServerApiContract.API_TAG_PORK);
            boolean tagCow = tags.getBoolean(ServerApiContract.API_TAG_COW);
            boolean tagCowAw = tags.getBoolean(ServerApiContract.API_TAG_COW_AW);
            boolean tagVegan = tags.getBoolean(ServerApiContract.API_TAG_VEGAN);
            boolean tagVeg = tags.getBoolean(ServerApiContract.API_TAG_VEG);

            newMeal = new Meal(mealName, mealId);
            newMeal.setTag(Meal.TAG_BIO, tagBio);
            newMeal.setTag(Meal.TAG_FISH, tagFish);
            newMeal.setTag(Meal.TAG_PORK, tagPork);
            newMeal.setTag(Meal.TAG_COW, tagCow);
            newMeal.setTag(Meal.TAG_COW_AW, tagCowAw);
            newMeal.setTag(Meal.TAG_VEGAN, tagVegan);
            newMeal.setTag(Meal.TAG_VEG, tagVeg);
            newMeal.setGlobalRating(globalRating);
            newMeal.setUserRating(userRating);
            newMeal.setIngredients(ingredients);
            newMeal.setImages(imageUris);

            return newMeal;
        }

        private String getJsonString(int mealId, String userId) throws JSONException {
            String requestJsonStr = "";

            JSONObject request = new JSONObject();
            request.put(ServerApiContract.API_MEAL_ID, mealId);
            request.put(ServerApiContract.API_USER_ID, userId);
            requestJsonStr = request.toString();

            return requestJsonStr;
        }
    }

    private class DownloadPictureTask extends AsyncTask<String, Void, Bitmap[]> {

        /**
         * {@inheritDoc}
         */
        @Override
        protected Bitmap[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            Bitmap[] bmps = new Bitmap[params.length];

            for (int i = 0; i < params.length; i++) {
                HttpURLConnection urlConnection = null;
                int responseCode = -1;
                Bitmap result = null;

                try {
                    URL url = new URL(params[i]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    responseCode = urlConnection.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream input = urlConnection.getInputStream();
                        result = BitmapFactory.decodeStream(input);
                        input.close();
                    }

                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);
                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                bmps[i] = result;
            }



            return bmps;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(Bitmap[] bmps) {
            GalleryAdapter galleryAdapter = new GalleryAdapter(getActivity());
            if (bmps != null) {
                for (int i = 0; i < bmps.length; i++) {
                    galleryAdapter.addBitmap(bmps[i]);
                }
            }
            gallery.setAdapter(galleryAdapter);
            super.onPostExecute(bmps);
        }
    }

    private class RateMealTask extends AsyncTask<Integer, Void, Integer> {

        /**
         * {@inheritDoc}
         */
        @Override
        protected Integer doInBackground(Integer... params) {
            if (params.length < 2) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            OutputStreamWriter writer = null;
            String token = getToken();
            int response = -1;

            try {
                String output = getJsonString(params[0], params[1], token);
                URL url = ServerApiContract.getURL(ServerApiContract.PATH_RATING);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(output);
                writer.flush();

                InputStream input = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                JSONObject newMeal = new JSONObject(result.toString());
                JSONObject data = newMeal.getJSONObject(ServerApiContract.API_MEAL_DATA);
                JSONObject ratings = data.getJSONObject(ServerApiContract.API_MEAL_RATINGS);
                meal.setGlobalRating((int) (ratings.getDouble(ServerApiContract.API_GLOBAL_RATING)));
                meal.setUserRating(ratings.getInt(ServerApiContract.API_USER_RATING));
                response = urlConnection.getResponseCode();

            } catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return response;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return response;
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
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return response;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(Integer response) {
            if (getActivity() != null) {
                if (response == HttpURLConnection.HTTP_OK) {
                    updateScreen(false);
                    Toast.makeText(getActivity(), getString(R.string.toast_rating_succesfull), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_rating_failed), Toast.LENGTH_SHORT).show();
                }
            }
            super.onPostExecute(response);
        }

        private String getJsonString(int mealid, int value, String userid) throws JSONException {
            String ratingJsonStr = "";

            JSONObject rating = new JSONObject();
            rating.put(ServerApiContract.API_MEAL_ID, mealid);
            rating.put(ServerApiContract.API_RATING_VALUE, value);
            rating.put(ServerApiContract.API_USER_ID, userid);
            ratingJsonStr = rating.toString();

            return ratingJsonStr;
        }
    }

    private class MergeMealTask extends AsyncTask<Integer, Void, Integer> {

        /**
         * {@inheritDoc}
         */
        @Override
        protected Integer doInBackground(Integer... params) {
            if (params.length < 2) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            OutputStreamWriter writer = null;
            Integer response = -1;
            String token = getToken();

            try {
                String output = getJsonString(params[0], params[1], token);

                URL url = ServerApiContract.getURL(ServerApiContract.PATH_MERGE);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(output);
                writer.flush();

                response = urlConnection.getResponseCode();
            } catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return response;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return response;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return response;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(Integer response) {
            if (getActivity() != null) {
                if (response == HttpURLConnection.HTTP_OK) {
                    Toast.makeText(getActivity(), getString(R.string.toast_merge_succesfull), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_merge_failed), Toast.LENGTH_SHORT).show();
                }
            }
            super.onPostExecute(response);
        }

        private String getJsonString(int fistMealId, int secondMealId, String userid) throws JSONException {
            String ratingJsonStr = "";

            JSONObject rating = new JSONObject();
            rating.put(ServerApiContract.API_USER_ID, userid);
            rating.put(ServerApiContract.API_FIRST_MEAL_ID, fistMealId);
            rating.put(ServerApiContract.API_SECOND_MEAL_ID, secondMealId);

            ratingJsonStr = rating.toString();

            return ratingJsonStr;
        }
    }
}