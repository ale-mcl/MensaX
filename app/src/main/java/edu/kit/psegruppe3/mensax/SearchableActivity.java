package edu.kit.psegruppe3.mensax;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.kit.psegruppe3.mensax.data.CanteenContract;

/**
 * Activity that performs searches and present results.
 */
public class SearchableActivity extends ListActivity {

    private SearchListAdapter mSearchListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        ListView listView = getListView();
        mSearchListAdapter = new SearchListAdapter(this, null, 0);
        setListAdapter(mSearchListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(SearchableActivity.this, DetailActivity.class)
                            .putExtra(DetailActivity.ARG_MEAL_ID,
                                    cursor.getInt(cursor.getColumnIndex(CanteenContract.MealEntry.COLUMN_MEAL_ID)));
                    startActivity(intent);
                }
            }
        });

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    public void doMySearch(String query) {
        Cursor cursor = query(query);
        mSearchListAdapter.swapCursor(cursor);
    }


    private Cursor query(String query) {
        Cursor cursor = getContentResolver().query(CanteenContract.MealEntry.CONTENT_URI, null,
                CanteenContract.MealEntry.COLUMN_MEAL_NAME + " LIKE ?",
                new String[]{"%" + query + "%"},
                null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
}
