package edu.kit.psegruppe3.mensax;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactFragment extends Fragment {

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        String contactUs = "Specific comments and general or private inquires can be send to:\n"+
                "mensaxapp@web.de";

        TextView txtContact = (TextView) rootView.findViewById(R.id.txtContact);
        txtContact.setText(contactUs);

        return rootView;
    }

}