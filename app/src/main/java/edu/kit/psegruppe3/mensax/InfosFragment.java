package edu.kit.psegruppe3.mensax;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class InfosFragment extends Fragment {

    public InfosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_infos, container, false);



        String legende = "(1) mit Farbstoff  \n(2) mit Konservierungsstoff \n(3) mit Antioxidationsmittel\n" +
                "(4) mit Geschmacksverstärker \n(5) mit Phosphat \n(6) Oberfläche gewachst\n" +
                "(7) geschwefelt \n(8) Oliven geschwärzt \n(9) mit Süßungsmittel\n" +
                "(10) - kann bei übermäßigem Verzehr abführend wirken\n" +
                "(11) - enthält eine Phenylalaninquelle\n" +
                "(12) - kann Restalkohol enthalten\n" +
                "(14) - aus Fleischstücken zusammengefügt\n" +
                "(15) - mit kakaohaltiger Fettglasur\n" +
                "(27) - aus Fischstücken zusammengefügt";


        TextView txtLegende = (TextView) rootView.findViewById(R.id.txtLegende);
        txtLegende.setText(legende);

        String address = "Mittagessen\n" +
                "Mo. - Fr. 11:00 - 14:00 Uhr\n" +
                "Curry Queen\n" +
                "Mo. - Do. 11:00 - 14:30 Uhr\n" +
                "Fr. 11:00 - 14:00 Uhr\n" +
                "Abendessen in der Cafeteria am Adenauerring\n" +
                "Mo - Do 16:00 - 19:30 Uhr";
        TextView textAddress = (TextView) rootView.findViewById(R.id.txtAddress);
        textAddress.setText(address);


        return rootView;
    }



}
