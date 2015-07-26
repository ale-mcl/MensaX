package edu.kit.psegruppe3.mensax;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 *Activity that shows the details about a meal.
 *
 * @author MensaX-group
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class DetailActivity extends ActionBarActivity {

    /**
     * The id of a meal
     */
    static final String ARG_MEAL_ID = "mealId";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int selectedMealId = intent.getIntExtra(ARG_MEAL_ID, 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_MEAL_ID, selectedMealId);

        DetailFragment detailData = new DetailFragment();
        detailData.setArguments(bundle);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detailContainer, detailData)
                    .commit();
            }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detailactivity_menu, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;

        } else if (id == R.id.action_search) {
            onSearchRequested();
            return true;

        } else if (id == R.id.action_legende) {
            LegendeFragment legendeFragment = new LegendeFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.detailContainer, legendeFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.action_contact) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);

        }else if (id == R.id.action_liveCam) {
            Intent intent = new Intent(this, LiveCamsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if(fm.getBackStackEntryCount() > 0 && findViewById(R.id.legendeContainer).getVisibility() == View.VISIBLE) {
            findViewById(R.id.legendeContainer).setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
