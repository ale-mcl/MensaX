package edu.kit.psegruppe3.mensax;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.kit.psegruppe3.mensax.datamodels.Meal;

/**
 * Fragment of the activity DetailActivity that shows all info about a meal.
 */
public class DetailFragment extends Fragment {

    private Meal meal;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        int mealId = getArguments().getInt("selectedMealId");
        /* HERE CODE TO:
                use the mealId to select the meal from the sql table;
         */

        Button button1 = (Button) rootView.findViewById(R.id.button_foodMerge);
        Button button2 = (Button) rootView.findViewById(R.id.button_takePicture);

        TextView textView = (TextView) rootView.findViewById(R.id.textview_details);
        //textView.setText(meal.getName());

        return rootView;
    }

    private void uploadImage(Uri imageUri) {
    }

    private void rate(int rating) {
    }

    private void mergeMeals(Meal secondMeal) {
    }

    private Uri selectImageUri() {
        return null;
    }

    private Meal selectMeal() {
        return null;
    }

    static class FetchMealDataTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}