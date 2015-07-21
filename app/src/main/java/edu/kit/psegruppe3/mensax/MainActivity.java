package edu.kit.psegruppe3.mensax;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

import edu.kit.psegruppe3.mensax.sync.MensaXSyncAdapter;


public class MainActivity extends ActionBarActivity {

    static final int NUM_TABS = 5;
    static final String ARG_TAB_NUM = "num";

    private TabAdapter mAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        mAdapter = new TabAdapter(getSupportFragmentManager(), this);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mPager);

        MensaXSyncAdapter.initializeSyncAdapter(this);
        MensaXSyncAdapter.syncImmediately(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
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

    public static class TabAdapter extends FragmentPagerAdapter {

        private Context context;

        public TabAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = new DailyMenuFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_TAB_NUM, position);
            f.setArguments(bundle);
            return f;
        }


        @Override
        public int getCount() {
            return NUM_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Calendar calendar = Calendar.getInstance();
            int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
            int day = (currentDay + position);
            if (day > 6) {
                day = day - 5;
            }
            if (day == currentDay) {
                return context.getString(R.string.today);
            } else if (day == currentDay + 1) {
                return context.getString(R.string.tomorrow);
            } else {
                switch (day) {
                    case Calendar.MONDAY: return context.getString(R.string.monday);
                    case Calendar.TUESDAY: return context.getString(R.string.tuesday);
                    case Calendar.WEDNESDAY: return context.getString(R.string.wednesday);
                    case Calendar.THURSDAY: return context.getString(R.string.thursday);
                    case Calendar.FRIDAY: return context.getString(R.string.friday);
                    default: return super.getPageTitle(position);
                }
            }
        }
    }

}
