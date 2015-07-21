package edu.kit.psegruppe3.mensax;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.kit.psegruppe3.mensax.data.CanteenContract;
import edu.kit.psegruppe3.mensax.datamodels.*;

/**
 * The fragment of the MainActivity. It shows the daily menu.
 */
public class DailyMenuFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private DailyMenu mDailyMenu;
    private ExpandableListView mListView;

    private static final int MENU_LOADER = 0;

    public DailyMenuFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        getLoaderManager().initLoader(MENU_LOADER, bundle, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_daily_menu, container, false);


        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ExpandableListView) rootView.findViewById(R.id.offer_listview);

        return rootView;
    }

    private ExpandableListView.OnChildClickListener myChildItemClicked =  new ExpandableListView.OnChildClickListener() {
        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
            //the method getChildId from the ExpandableListAdapter returns a primitive type long
            long selectedMealId_long = parent.getExpandableListAdapter().getChildId(groupPosition, childPosition);
            int selectedMealId = (int) selectedMealId_long;
            //send the id of the meal clicked to the DetailActivity and start it.
            Intent intent = new Intent(getActivity(), DetailActivity.class)
                    .putExtra(DetailActivity.ARG_MEAL_ID, selectedMealId);
            startActivity(intent);
            return false;
        }
    };


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Long date = args.getLong(MainActivity.ARG_DATE);

        String sortOrder = CanteenContract.OfferEntry.COLUMN_PRICE_STUDENTS + " DESC";


        Uri mensaOfferUri = CanteenContract.OfferEntry.buildOfferDate(date);

        return new CursorLoader(getActivity(),
                mensaOfferUri,
                null,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }
        int columnMealName = data.getColumnIndex(CanteenContract.MealEntry.COLUMN_MEAL_NAME);
        int columnMealId = data.getColumnIndex(CanteenContract.MealEntry.COLUMN_MEAL_ID);
        int columnLine = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_LINE);
        int columnPriceStudents = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_PRICE_STUDENTS);
        int columnPriceStaff = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_PRICE_STAFF);
        int columnPricePupils = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_PRICE_PUPILS);
        int columnPriceGuests = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_PRICE_GUESTS);
        int columnRating = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_GLOBAL_RATING);
        int columnTagBio = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_TAG_BIO);
        int columnTagFish = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_TAG_FISH);
        int columnTagPork = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_TAG_PORK);
        int columnTagCow = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_TAG_COW);
        int columnTagCowAw = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_TAG_COW_AW);
        int columnTagVegan = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_TAG_VEGAN);
        int columnTagVeg = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_TAG_VEG);
        int columnIngredients = data.getColumnIndex(CanteenContract.OfferEntry.COLUMN_INGREDIENTS);
        List<Offer> offerList = new ArrayList<Offer>();
        do {
            String mealName = data.getString(columnMealName);
            int mealId = data.getInt(columnMealId);
            int priceStudents = data.getInt(columnPriceStudents);
            int priceStaff = data.getInt(columnPriceStaff);
            int pricePupils = data.getInt(columnPricePupils);
            int priceGuests = data.getInt(columnPriceGuests);
            boolean tagBio = (data.getInt(columnTagBio) != 0);
            boolean tagFish = (data.getInt(columnTagFish) != 0);
            boolean tagPork = (data.getInt(columnTagPork) != 0);
            boolean tagCow = (data.getInt(columnTagCow) != 0);
            boolean tagCowAw = (data.getInt(columnTagCowAw) != 0);
            boolean tagVegan = (data.getInt(columnTagVegan) != 0);
            boolean tagVeg = (data.getInt(columnTagVeg) != 0);
            String ingredients = data.getString(columnIngredients);
            String line = data.getString(columnLine);
            Meal meal = new Meal(mealName, mealId);
            meal.setTag(Meal.TAG_BIO, tagBio);
            meal.setTag(Meal.TAG_FISH, tagFish);
            meal.setTag(Meal.TAG_PORK, tagPork);
            meal.setTag(Meal.TAG_COW, tagCow);
            meal.setTag(Meal.TAG_COW_AW, tagCowAw);
            meal.setTag(Meal.TAG_VEGAN, tagVegan);
            meal.setTag(Meal.TAG_VEG, tagVeg);
            meal.setIngredients(ingredients);
            offerList.add(new Offer(meal, getLine(line), priceStudents, priceGuests, priceStaff, pricePupils));
        } while (data.moveToNext());
        Offer[] offers = offerList.toArray(new Offer[offerList.size()]);
        mDailyMenu = new DailyMenu(0, offers);
        updateList();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void updateList() {
        final OfferListAdapter mainActivityFragmentAdapter = new OfferListAdapter(getActivity(), mDailyMenu);

        mListView.setAdapter(mainActivityFragmentAdapter);

        mListView.setOnChildClickListener(myChildItemClicked);

        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int groupCount = mainActivityFragmentAdapter.getGroupCount();
                for (int i = 0; i < groupCount; i++) {
                    if (i != groupPosition) {
                        mListView.collapseGroup(i);
                    }
                }
            }
        });
    }

    private Line getLine(String line) {
        if (line.equals("l1")) {
            return Line.l1;
        } else if (line.equals("l2")) {
            return Line.l2;
        } else if (line.equals("l3")) {
            return Line.l3;
        } else if (line.equals("l45")) {
            return Line.l45;
        } else if (line.equals("schnitzelbar")) {
            return Line.schnitzelbar;
        } else if (line.equals("update")) {
            return Line.update;
        } else if (line.equals("abend")) {
            return Line.abend;
        } else if (line.equals("aktion")) {
            return Line.aktion;
        } else if (line.equals("heisstheke")) {
            return Line.heisstheke;
        } else {
            return Line.nmtisch;
        }
    }
}