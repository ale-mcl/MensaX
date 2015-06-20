package edu.kit.psegruppe3.mensax;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.kit.psegruppe3.mensax.datamodels.DailyMenu;
import edu.kit.psegruppe3.mensax.datamodels.Line;
import edu.kit.psegruppe3.mensax.datamodels.Meal;
import edu.kit.psegruppe3.mensax.datamodels.Offer;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {

    private DailyMenu mDailyMenu;


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //this menu is not done yet
        //inflater.inflate(R.menu.mainactivityfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        mDailyMenu = createExampleDailyMenu();
        ExpandableListAdapter mainActivityFragmentAdapter = new OfferListAdapter(getActivity(), mDailyMenu);

        // Get a reference to the ListView, and attach this adapter to it.
        ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.offer_listview);
        listView.setAdapter(mainActivityFragmentAdapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String forecast = mainActivityFragmentAdapter.g
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });*/

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        startActivity(intent);
    }

    private DailyMenu createExampleDailyMenu() {
        Meal meal1 = new Meal("Linseneintopf", 324);
        Meal meal2 = new Meal("Spaghetti Carbonara", 234);
        Meal meal3 = new Meal("Pommes", 254);
        Meal meal4 = new Meal("Grüner Salat", 456);
        Meal meal5 = new Meal("Currywurst", 765);
        Meal meal6 = new Meal("Kroketten", 453);
        Meal meal7 = new Meal("Gebratene Hänchenkeule", 893);
        Offer offer1 = new Offer(meal1, Line.l1, 250, 123, 231, 432);
        Offer offer2 = new Offer(meal2, Line.l1, 250, 123, 231, 432);
        Offer offer3 = new Offer(meal3, Line.l2, 100, 123, 231, 432);
        Offer offer4 = new Offer(meal4, Line.l3, 50, 123, 231, 432);
        Offer offer5 = new Offer(meal5, Line.aktion, 350, 123, 231, 432);
        Offer offer6 = new Offer(meal6, Line.l45, 100, 123, 231, 432);
        Offer offer7 = new Offer(meal3, Line.l3, 100, 123, 231, 432);
        Offer offer8 = new Offer(meal7, Line.l1, 285, 123, 231, 432);
        Offer offer9 = new Offer(meal7, Line.l1, 285, 123, 231, 432);
        Offer offer10 = new Offer(meal3, Line.l45, 100, 123, 231, 432);
        Offer offer11 = new Offer(meal3, Line.schnitzelbar, 100, 123, 231, 432);
        Offer offer12 = new Offer(meal4, Line.l45, 100, 123, 231, 432);
        Offer offer13 = new Offer(meal4, Line.l2, 100, 123, 231, 432);
        Offer[] offers = {offer1, offer2, offer3, offer4, offer5, offer6, offer7, offer8, offer9, offer10, offer11, offer12, offer12, offer13};
        return new DailyMenu(System.currentTimeMillis(), offers);
    }
}
