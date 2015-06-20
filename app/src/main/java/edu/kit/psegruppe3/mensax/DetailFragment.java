package edu.kit.psegruppe3.mensax;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.kit.psegruppe3.mensax.datamodels.Meal;

/**
 * Created by ekremsenturk on 20.06.15.
 */
public class DetailFragment extends Fragment {

    private Meal meal;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        String offer_data = getArguments().getString("offer_data");
        TextView textView = (TextView) rootView.findViewById(R.id.textview_details);
        textView.setText(offer_data);


        return rootView;
    }

    private void loadImage(Uri imageUri) {
        
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
}
