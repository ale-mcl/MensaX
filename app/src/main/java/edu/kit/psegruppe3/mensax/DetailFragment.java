package edu.kit.psegruppe3.mensax;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
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
        meal.setGlobalRating(3.5f);


        //int mealId = getArguments().getInt("selectedMealId");
        /* HERE CODE TO:
                use the mealId to select the meal from the sql table;
         */

        TextView txtMealName = (TextView) rootView.findViewById(R.id.mealName);
        txtMealName.setText(meal.getName());

        TextView txtMealIngredients = (TextView) rootView.findViewById(R.id.mealIngredients);
        txtMealIngredients.setText("Ingredients:");

        TextView txtShowIngredients = (TextView) rootView.findViewById(R.id.showIngredients);
        txtShowIngredients.setText("[" + meal.getIngredients() + "]");

        TextView txtMealGlobalRating = (TextView) rootView.findViewById(R.id.mealGlobalRating);
        txtMealGlobalRating.setText("Rating:");

        RatingBar rtbarShowGlobalRating = (RatingBar) rootView.findViewById(R.id.showGlobalRating);
        rtbarShowGlobalRating.setRating(meal.getGlobalRating());

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