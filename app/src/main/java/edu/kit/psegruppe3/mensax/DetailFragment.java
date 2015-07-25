package edu.kit.psegruppe3.mensax;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
 */
public class DetailFragment extends Fragment {

    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    private Meal meal;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private TextView txtMealName;
    private TextView txtIngredients;
    private RatingBar globalRating;
    private RatingBar userRating;

    private String[] picturesURLs;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        Integer[] mealId = {getArguments().getInt(DetailActivity.ARG_MEAL_ID)};

        Gallery gallery = (Gallery) rootView.findViewById(R.id.gallery1);
        gallery.setAdapter(new ImageAdapter(getActivity()));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,long id)
            {
                // display the images selected
               // ImageView imageView = (ImageView) getActivity().findViewById(R.id.image1);
               // imageView.setImageResource(imageIDs[position]);
            }
        });

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
        });

        Button btnTakePicture = (Button) rootView.findViewById(R.id.button_takePicture);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePicture(v);
            }
        });

        Button btnMergeMeal = (Button) rootView.findViewById(R.id.button_mergeMeal);
        btnMergeMeal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                askSearchQuery();
            }
        });

        return rootView;
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // start the image capture Intent
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            String stringOfPhoto = BitMapToString(photo);
            //UploadPictureTask uploadPictureTask = new UploadPictureTask();
            //uploadPictureTask.execute(String.valueOf(meal.getMealId()), stringOfPhoto);

        } else if (requestCode == SearchableActivity.REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            if (data.hasExtra(DetailActivity.ARG_MEAL_ID)) {
                int mealId = data.getIntExtra(DetailActivity.ARG_MEAL_ID, 0);
                MergeMealTask mergeMealTask = new MergeMealTask();
                mergeMealTask.execute(meal.getMealId(), mealId);
            }
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private class UploadPictureTask extends AsyncTask<String,Void, Void> {

        private final String LOG_TAG = UploadPictureTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {
            if (params.length < 2) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            OutputStreamWriter writer = null;

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

                Log.d("doInBackground(Request)", output);

                writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(output);
                writer.flush();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
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
    }

    private void updateScreen() {
        if (meal == null) {
            return;
        }
        txtMealName.setText(meal.getName());

        txtIngredients.setText("[" + meal.getIngredients() + "]");

        globalRating.setRating((float) meal.getGlobalRating());

        userRating.setRating((float) meal.getUserRating());
    }

    private String getToken() {
        final String scope = "audience:server:client_id:785844054287-7hge652kf27md81acog9vg1u0nk9so83.apps.googleusercontent.com";
        try {
            String email = getGoogleEmailAdress();
            String token = GoogleAuthUtil.getToken(getActivity(), email, scope);
            Log.d(LOG_TAG, "Token: " + token);
            return token;
        } catch (IOException e) {
            Log.d(LOG_TAG, "Error");
        } catch (UserRecoverableAuthException e ) {
            Log.d(LOG_TAG, "Error");
        } catch (GoogleAuthException e ) {
            Log.d(LOG_TAG, "Error");
        }
        return null;
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


    private class FetchMealDataTask extends AsyncTask<Integer, Void, Meal> {

        private final String LOG_TAG = FetchMealDataTask.class.getSimpleName();

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

                //Log.d("doInBackground(Request)", output);

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
                //Log.d("doInBackground(Resp)", result.toString());

                newMeal = getMealDataFromJson(result.toString());
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
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
            return newMeal;
        }

        @Override
        protected void onPostExecute(Meal m) {
            if (meal != null) {
                meal = m;
                updateScreen();
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

            return newMeal;
        }

        private String getJsonString(int mealId, String userId) throws JSONException {
            String ratingJsonStr = "";

            JSONObject rating = new JSONObject();
            rating.put(ServerApiContract.API_MEAL_ID, mealId);
            rating.put(ServerApiContract.API_USER_ID, userId);
            ratingJsonStr = rating.toString();

            return ratingJsonStr;
        }
    }

    private class DownloadPictureTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            OutputStreamWriter writer = null;
            int responseCode = -1;
            Bitmap result = null;

            try {
                URL url = new URL(params[0]);
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
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //TODO: Add bitmap to UI here
            super.onPostExecute(bitmap);
        }
    }

    // use like this: rateMealTask.execute(mealid, ratingvalue);
    private class RateMealTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            if (params.length < 2) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            OutputStreamWriter writer = null;
            String token = getToken();

            try {
                String output = getJsonString(params[0], params[1], token);
                URL url = ServerApiContract.getURL(ServerApiContract.PATH_RATING);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                //Log.d("doInBackground(Request)", output);

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
                //Log.d("doInBackground(Resp)", result.toString());
                JSONObject newMeal = new JSONObject(result.toString());
                JSONObject data = newMeal.getJSONObject("data");
                JSONObject ratings = data.getJSONObject("ratings");
                meal.setGlobalRating((int) (ratings.getDouble("average") * 100));
                meal.setUserRating((ratings.getInt("currentUserRating") * 100));
            } catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
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
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            updateScreen();
            super.onPostExecute(aVoid);
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

    private class MergeMealTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            if (params.length < 2) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            OutputStreamWriter writer = null;
            BufferedReader reader = null;
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

                //Log.d("doInBackground(Request)", output);

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


            } catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
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
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
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

    public void sendSearchRequest(String query) {
        Intent mIntent = new Intent(getActivity(), SearchableActivity.class);
        mIntent.setAction(Intent.ACTION_SEARCH);
        mIntent.putExtra(SearchableActivity.ARG_SELECT_MEAL, true);
        mIntent.putExtra(SearchManager.QUERY, query);
        startActivityForResult(mIntent, SearchableActivity.REQUEST_CODE);
    }

    public void askSearchQuery() {
        final EditText input = new EditText(getActivity());
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Search Items");
        adb.setMessage("Please input the name of the item you are looking for.");
        adb.setView(input);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Editable upc = input.getText();
                sendSearchRequest(upc.toString());

                dialog.cancel();
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        adb.create().show();
    }


    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;
        public ImageAdapter(Context c)
        {
            context = c;
            // sets a grey background; wraps around the images
            TypedArray a = context.obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();
        }
        // returns the number of images
        public int getCount() {
            return picturesURLs.length;
        }
        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }
        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }
        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);

            imageView.setImageBitmap(getBitmapFromUrl(picturesURLs[position]));
            imageView.setLayoutParams(new Gallery.LayoutParams(600, 600));
            imageView.setBackgroundResource(itemBackground);
            return imageView;
        }

        private Bitmap getBitmapFromUrl (String url){
            Bitmap bitmap = null;
            /*
            TO-DO HERE:
            write method  that gets a string of an url and returns a bitmap.
            */
            return bitmap;
        }
    }



}