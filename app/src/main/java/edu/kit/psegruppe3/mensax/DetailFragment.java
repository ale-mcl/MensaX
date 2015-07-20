package edu.kit.psegruppe3.mensax;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
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
        final View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        meal = new Meal("Linseneintopf", 324);
        meal.setTag(Meal.TAG_COW, true);
        meal.setIngredients("2,3,Gl,Se,Sf,Sn,So");
        meal.setGlobalRating(3.5f);


        //int mealId = getArguments().getInt("selectedMealId");
        /* HERE CODE TO:
                use the mealId to select the meal from the sql table;
         */

        TextView txtMealName = (TextView) rootView.findViewById(R.id.mealName);
        txtMealName.setText(meal.getName());

        TextView txtMealIngredients = (TextView) rootView.findViewById(R.id.mealIngredients);
        txtMealIngredients.setText(R.string.ingredients);

        TextView txtShowIngredients = (TextView) rootView.findViewById(R.id.showIngredients);
        txtShowIngredients.setText("[" + meal.getIngredients() + "]");

        TextView txtMealGlobalRating = (TextView) rootView.findViewById(R.id.mealGlobalRating);
        txtMealGlobalRating.setText(R.string.global_rating);

        RatingBar rtbarShowGlobalRating = (RatingBar) rootView.findViewById(R.id.showGlobalRating);
        rtbarShowGlobalRating.setRating(meal.getGlobalRating());

        TextView txtMealUserRating = (TextView) rootView.findViewById(R.id.mealUserRating);
        txtMealUserRating.setText(R.string.user_rating);

        RatingBar rtbarShowUserRating = (RatingBar) rootView.findViewById(R.id.showUserRating);
        rtbarShowUserRating.setRating(meal.getUserRating());

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