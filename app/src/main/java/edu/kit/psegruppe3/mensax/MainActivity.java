package edu.kit.psegruppe3.mensax;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Notice that setContentView() is not used, because we use the root
        // android.R.id.content as the container for each fragment

        // setup action bar for tabs
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        ActionBar.Tab tab = actionBar.newTab()
                .setText(R.string.monday)
                .setTabListener(new TabListener<>(
                        this, "1", MainActivityFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.tuesday)
                .setTabListener(new TabListener<>(
                        this, "2", MainActivityFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.wednesday)
                .setTabListener(new TabListener<>(
                        this, "3", MainActivityFragment.class));
        actionBar.addTab(tab);
        tab = actionBar.newTab()
                .setText(R.string.thursday)
                .setTabListener(new TabListener<>(
                        this, "4", MainActivityFragment.class));
        actionBar.addTab(tab);
        tab = actionBar.newTab()
                .setText(R.string.friday)
                .setTabListener(new TabListener<>(
                        this, "5", MainActivityFragment.class));
        actionBar.addTab(tab);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
