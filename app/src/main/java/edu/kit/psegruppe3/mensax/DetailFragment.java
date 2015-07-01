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
import edu.kit.psegruppe3.mensax.datamodels.Tag;

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

        meal = new Meal("Linseneintopf", 324);
        meal.setTag(Tag.BEEF);
        meal.setIngredients("2,3,Gl,Se,Sf,Sn,So");
        meal.setGlobalRating(3);


        //int mealId = getArguments().getInt("selectedMealId");
        /* HERE CODE TO:
                use the mealId to select the meal from the sql table;
         */

        TextView textView1 = (TextView) rootView.findViewById(R.id.mealName);
        textView1.setText(meal.getName());

        TextView textView2 = (TextView) rootView.findViewById(R.id.mealIngredients);
        textView2.setText("Ingredients:");

        TextView textView3 = (TextView) rootView.findViewById(R.id.showIngredients);
        textView3.setText("[" + meal.getIngredients() + "]");

        TextView textView4 = (TextView) rootView.findViewById(R.id.mealGlobalRating);
        textView4.setText("Rating:");

        String globalRating = String.valueOf(meal.getGlobalRating());
        TextView textView5 = (TextView) rootView.findViewById(R.id.showGlobalRating);
        textView5.setText(globalRating);

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