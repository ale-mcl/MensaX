package edu.kit.psegruppe3.mensax;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment of the DetailActivity class that shows the legende for the food ingredients.
 *
 * @author MensaX-group
 * @verion 1.0
 */
public class LegendeFragment extends Fragment {

    /**
     * Constructor of the class.
     */
    public LegendeFragment() {
        // Required empty public constructor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_legende, container, false);
    }

}
