package edu.kit.psegruppe3.mensax;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import edu.kit.psegruppe3.mensax.data.CanteenContract;

/**
 * Activity that performs searches on the meal database and present results.
 *
 * @author MensaX-group
 * @version 1.1
 */
public class SearchableActivity extends ActionBarActivity {

    /**
     * CODE to determine if the search request is a merge request.
     */
    public static final int REQUEST_CODE = 1337;
    /**
     * The argument parameter of the searched meal.
     */
    public static final String ARG_SELECT_MEAL = "selectMeal";
    private SearchListAdapter mSearchListAdapter;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        ListView listView = (ListView) findViewById(R.id.search_listview);
        mSearchListAdapter = new SearchListAdapter(this, null, 0);
        listView.setAdapter(mSearchListAdapter);
        final Intent intent = getIntent();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    int mealId = cursor.getInt(cursor.getColumnIndex(CanteenContract.MealEntry.COLUMN_MEAL_ID));
                    if (intent.hasExtra(ARG_SELECT_MEAL) && intent.getBooleanExtra(ARG_SELECT_MEAL, false)) {
                        Intent result = new Intent(SearchableActivity.this, DetailActivity.class)
                                .putExtra(DetailActivity.ARG_MEAL_ID, mealId);
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    } else {
                        Intent result = new Intent(SearchableActivity.this, DetailActivity.class)
                                .putExtra(DetailActivity.ARG_MEAL_ID, mealId);
                        startActivity(result);
                    }
                }
            }
        });

        // Get the intent, verify the action and get the query
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }

        if (listView.getCount() == 0) {
            TextView emptyTextView = (TextView) findViewById(R.id.search_empty_textview);
            emptyTextView.setVisibility(View.VISIBLE);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method that executes the search request.
     * @param query the query of the search
     */
    private void doMySearch(String query) {
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
