package edu.kit.psegruppe3.mensax;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;


public class DetailActivity extends ActionBarActivity {

    static final String ARG_MEAL_ID = "mealId";

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
                    .add(R.id.container, detailData)
                    .commit();
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detailactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_search) {
            onSearchRequested();
            return true;
        } else if (id == R.id.action_legende) {

            InfosFragment infosFragment = new InfosFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.container, infosFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }
}
