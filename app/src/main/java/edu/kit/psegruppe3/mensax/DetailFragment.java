package edu.kit.psegruppe3.mensax;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        int selectedMealId = getArguments().getInt("selectedMealId");

        //HERE goes the code to: from the mealId select the meal from the table.

        TextView textView = (TextView) rootView.findViewById(R.id.textview_details);
        //catching NullPointerException because atm we have
        // no real data of a meal to give to the detailActivity.
        try {
            textView.setText(meal.getName());
        } catch (NullPointerException e) {
            String ops = "no real data yet";
            textView.setText(ops);
        }


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