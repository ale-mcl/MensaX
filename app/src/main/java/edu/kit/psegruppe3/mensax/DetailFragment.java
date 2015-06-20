package edu.kit.psegruppe3.mensax;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ekremsenturk on 20.06.15.
 */
public class DetailFragment extends Fragment {

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
}
